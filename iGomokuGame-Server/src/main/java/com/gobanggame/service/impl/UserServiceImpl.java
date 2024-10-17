package com.gobanggame.service.impl;

import com.gobanggame.constant.UserConstant;
import com.gobanggame.exception.UserException;
import com.gobanggame.mapper.UserMapper;
import com.gobanggame.mapper.WebsiteDayInfoMapper;
import com.gobanggame.mapper.WebsiteVisitorDetailsInfoMapper;
import com.gobanggame.pojo.entity.WebsiteDayInfo;
import com.gobanggame.pojo.entity.WebsiteVisitorDetailsInfo;
import com.gobanggame.pojo.pojo.JwtProperties;
import com.gobanggame.pojo.dto.UserRegisterDTO;
import com.gobanggame.pojo.dto.UserUpdateDTO;
import com.gobanggame.pojo.entity.User;
import com.gobanggame.pojo.vo.RankingItem;
import com.gobanggame.pojo.vo.UserLoginVO;
import com.gobanggame.pojo.vo.UserSelectByIdVO;
import com.gobanggame.service.UserService;
import com.gobanggame.utils.IpUtils;
import com.gobanggame.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-09 21:24
 * @description: 用户service实现类
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebsiteDayInfoMapper websiteDayInfoMapper;

    @Autowired
    private WebsiteVisitorDetailsInfoMapper websiteVisitorDetailsInfoMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private HttpServletRequest request;

    /**
     * @param userRegisterDTO
     * @return void
     * @author AlbertZhang
     * @description 用户注册
     * @date 2023-12-09 21:25
     **/
    @Override
    @Transactional
    public void register(UserRegisterDTO userRegisterDTO) {
        // 业务需求：首先查询数据库，查找是否存在有相似的用户名
        User user = userMapper.selectByUsername(userRegisterDTO.getUsername());

        // 用户名重复了
        if (user != null) {
            throw new UserException(UserConstant.USERNAME_DUPLICATE);
        }

        // 进行注册，并加上公共字段填充(mapper层添加)
        // 这里需要拷贝一下，不然这个DTO里面没有时间，没办法自动填充
        User user1 = new User();
        BeanUtils.copyProperties(userRegisterDTO, user1);
        userMapper.register(user1);
        // 更新一下昵称
        user1.setNickname("不知名小将" + user1.getId());
        user1.setLastOnlineTime(LocalDateTime.now());
        userMapper.update(user1);

        // 维护网站每日记录表，新增一个用户
        WebsiteDayInfo websiteDayInfo1 = websiteDayInfoMapper.selectByDate(LocalDate.now());
        int count = 0;
        if (websiteDayInfo1 != null) {
            count = websiteDayInfo1.getNewUserCount();
        }
        WebsiteDayInfo websiteDayInfo = WebsiteDayInfo.builder().newUserCount(count + 1)
                .recordDate(LocalDate.now())
                .build();
        websiteDayInfoMapper.update(websiteDayInfo);
    }

    /**
     * @param userLoginDTO
     * @return com.gobanggame.pojo.vo.UserLoginVO
     * @author AlbertZhang
     * @description 用户登录
     * @date 2023-12-09 22:08
     **/
    @Override
    @Transactional
    public UserLoginVO login(UserRegisterDTO userLoginDTO) {
        /*
             登录的业务逻辑，首先查询数据库看看能不能查到数据
             查询不到返回用户名/密码错误的异常
             查询到就生成jwt令牌->组装数据->返回
         * */
        User user = userMapper.login(userLoginDTO);
        log.info("查询的用户信息{}", user);
        // 查不到数据
        if (user == null) {
            throw new UserException(UserConstant.USERNAME_OR_PASSWORD_WRONG);
        }
        // 生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("userRole", "user"); // 标识用户或管理员
        String jwt = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims);

        // 组装数据
        // 数据先拷一部分
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);

        // 剩下的数据，rankingList查一下，用于排行榜数据的显示
        List<RankingItem> rankingList = userMapper.selectRankingListDefault();
        userLoginVO.setRankingList(rankingList);
        userLoginVO.setJwt(jwt);
        log.info("查询的用户信息{}", userLoginVO);

        // 维护网站访客细节表和网站访客表，我们认为在今天一个ip的用户，都算做一个访客
        // 获取ip
        String ipAddr = IpUtils.getIpAddr(request);

        // 维护网站访客细节表，我们认为访问过网站并在1h内登录了，就更新用户名
        websiteVisitorDetailsInfoMapper.update(ipAddr, userLoginVO.getNickname());

        // 维护网站每日访客表，只能根据ip区分了，可能会少统计一部分，因为很多人用的是一个ip。
        // mapper会写好时间范围
        List<WebsiteVisitorDetailsInfo> websiteVisitorDetailsInfoList = websiteVisitorDetailsInfoMapper.selectByIpAddr(ipAddr);

        WebsiteDayInfo websiteDayInfo1 = websiteDayInfoMapper.selectByDate(LocalDate.now());
        if (websiteVisitorDetailsInfoList.size() == 1) {
            // 说明该用户今天只点击了一次，添加一次
            WebsiteDayInfo websiteDayInfo = WebsiteDayInfo.builder().visitorCount(websiteDayInfo1.getVisitorCount() + 1).recordDate(LocalDate.now()).build();
            websiteDayInfoMapper.update(websiteDayInfo);
        } else {
            // 0次或大于1次，0次不可能，点击的时候会存东西，因此这个就说明用户重复登录，不更新
            // log.info("用户重复登录：{}", userLoginVO.getNickname());
        }
        return userLoginVO;
    }

    /**
     * @param id
     * @return com.gobanggame.pojo.vo.UserSelectByIdVO
     * @author AlbertZhang
     * @description 根据id查询用户信息
     * @date 2023-12-09 23:15
     **/
    @Override
    public UserSelectByIdVO getById(Long id) {
        return userMapper.selectUserById(id);
    }

    /**
     * @param userUpdateDTO
     * @return void
     * @author AlbertZhang
     * @description 更新用户信息
     * @date 2023-12-09 23:22
     **/
    @Override
    @Transactional
    public void update(UserUpdateDTO userUpdateDTO) {
        /*  更新用户信息的要求：
         *       1：昵称必须唯一
         *       2：更新失败给用户返回更新失败的提示（如字段过长）
         *
         * */

        // 昵称必须唯一，首先看他改昵称了吗？
        User user1 = userMapper.selectByUsername(userUpdateDTO.getUsername());
        if (!Objects.equals(user1.getNickname(), userUpdateDTO.getNickname())) {
            // 改昵称了，看看昵称重复不重复
            User user2 = userMapper.selectByNickName(userUpdateDTO.getNickname(), userUpdateDTO.getId());
            if (user2 != null) {
                throw new UserException(UserConstant.NICKNAME_DUPLICATE);
            }
        }

        // 更新一下，实现公共字段填充

        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        int updated = userMapper.update(user);
        // 如果更新失败
        if (updated == 0) {
            throw new UserException(UserConstant.UPDATE_FAILED);
        }
    }

    /**
     * @param id
     * @return void
     * @author AlbertZhang
     * @description 根据id删除用户（逻辑删除）
     * @date 2023-12-25 15:39
     **/
    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = User.builder().id(id).deleted(1).build();
        // 调用条件更新
        int deleted = userMapper.update(user);
        // 更新失败
        if (deleted == 0) {
            throw new UserException(UserConstant.DELETE_FAILED);
        }
    }
}
