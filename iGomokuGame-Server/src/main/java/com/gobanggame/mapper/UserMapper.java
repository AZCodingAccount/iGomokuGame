package com.gobanggame.mapper;

import com.gobanggame.annotation.AutoFill;
import com.gobanggame.constant.OperationType;
import com.gobanggame.pojo.dto.UserRegisterDTO;
import com.gobanggame.pojo.entity.User;
import com.gobanggame.pojo.entity.UserFriends;
import com.gobanggame.pojo.vo.RankingItem;
import com.gobanggame.pojo.vo.UserSelectByIdVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-09 21:27
 * @description: 用户mapper
 **/
@Mapper
public interface UserMapper {


    @AutoFill(OperationType.INSERT)
    void register(User user);

    /**
     * @param userLoginDTO
     * @return com.gobanggame.pojo.entity.User
     * @author AlbertZhang
     * @description 查询用户
     * @date 2023-12-09 22:10
     **/
    @Select("select * from user where " +
            "username=#{username} and password=#{password} and deleted=0")
    User login(UserRegisterDTO userLoginDTO);


    /**
     * @param
     * @return java.util.List<com.gobanggame.pojo.vo.RankingItem>
     * @author AlbertZhang
     * @description 查询所有排行榜的数据
     * @date 2023-12-09 22:48
     **/
    @Select("select * from user where deleted=0 order by user_score desc ")
    List<RankingItem> selectRankingListDefault();

    /**
     * @param id
     * @return com.gobanggame.pojo.vo.UserSelectByIdVO
     * @author AlbertZhang
     * @description 根据id查询用户信息，实现数据回显操作
     * @date 2023-12-09 23:16
     **/
    @Select("select * from user where id=#{id} and deleted=0")
    UserSelectByIdVO selectUserById(Long id);

    /**
     * @param user
     * @return void
     * @author AlbertZhang
     * @description 根据传过来的数据动态更新
     * @date 2023-12-09 23:23
     **/
    @AutoFill(OperationType.UPDATE)
    int update(User user);

    // 下面的三个查询方法还是有些冗余，一个条件查询就完事了，但是需要写动态sql

    /**
     * @param username
     * @return com.gobanggame.pojo.entity.User
     * @author AlbertZhang
     * @description 根据用户名查询用户（完成用户名不重复的功能）
     * @date 2023-12-09 21:41
     **/
    @Select("select * from user where username=#{username} ")
    User selectByUsername(String username);

    /**
     * @param
     * @return com.gobanggame.pojo.entity.User
     * @author AlbertZhang
     * @description 根据昵称查询用户
     * @date 2023-12-10 18:43
     **/
    @Select("select * from user where nickname=#{nickname} and deleted=0 and id!=#{id}")
    User selectByNickName(String nickname, Long id);


    /**
     * @param userId
     * @param friendId
     * @return void
     * @author AlbertZhang
     * @description 根据用户id和friendId来判断当前是否是好友
     * @date 2023-12-20 16:07
     **/
    @Select("select * from user_friends where user_id=#{userId} and friend_id=#{friendId} and deleted=0")
    List<UserFriends> selectUserByIdAndFriendId(long userId, Long friendId);

    /**
     * @param
     * @return java.util.List<com.gobanggame.pojo.entity.User>
     * @author AlbertZhang
     * @description 获取用户列表，后台管理模块使用
     * @date 2023-12-19 20:57
     **/
    List<User> list(User user);

    // /**
    //  * @param id
    //  * @return void
    //  * @author AlbertZhang
    //  * @description 用户注销，逻辑删除
    //  * @date 2023-12-09 23:43
    //  **/
    // 这里还是使用update方法，这让不用再处理updatetime字段了
    // @Update("update user set deleted=1 where id=#{id} ")
    // void deleteById(Long id);
}
