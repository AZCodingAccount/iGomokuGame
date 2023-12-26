package com.gobanggame.service.impl;

import com.gobanggame.constant.FriendsConstant;
import com.gobanggame.exception.FriendsException;
import com.gobanggame.mapper.FriendsMapper;
import com.gobanggame.mapper.UserMapper;
import com.gobanggame.mapper.UserMessagesMapper;
import com.gobanggame.pojo.vo.MessageResult;
import com.gobanggame.pojo.dto.AddFriendsDTO;
import com.gobanggame.pojo.entity.User;
import com.gobanggame.pojo.entity.UserFriends;
import com.gobanggame.pojo.vo.UserFriendChatVO;
import com.gobanggame.pojo.vo.UserSelectByIdVO;
import com.gobanggame.service.FriendsService;
import com.gobanggame.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-10 21:15
 * @description: 好友service实现类
 **/
@Service
public class FriendsServiceImpl implements FriendsService {

    @Autowired
    private FriendsMapper friendsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserMessagesMapper userMessagesMapper;

    /**
     * @param
     * @return java.util.List<com.gobanggame.pojo.vo.UserSelectByIdVO>
     * @author AlbertZhang
     * @description 查询所有用户
     * @date 2023-12-10 21:26
     **/
    @Override
    public List<UserSelectByIdVO> list() {
        // 获取当前用户id
        Long id = BaseContext.getCurrentId();
        return friendsMapper.list(id);
    }

    /**
     * @param addFriendsDTO
     * @return void
     * @author AlbertZhang
     * @description 添加好友
     * @date 2023-12-10 21:42
     **/
    @Override
    @Transactional
    public void add(AddFriendsDTO addFriendsDTO) {
        /* 这里有一个业务需求，
            1：不能添加自己为好友
            2：当用户已注销都不能进行好友的添加，
            3：好友不存在不能进行好友的添加
            4：好友已存在也不能添加
            */
        long id = BaseContext.getCurrentId();

        UserSelectByIdVO user = userMapper.selectUserById(id);
        // 这里必须要用equals，因为可能存在null。1：不能添加自己为好友
        if (Objects.equals(user.getUsername(), addFriendsDTO.getUsername()) || Objects.equals(user.getNickname(), addFriendsDTO.getNickname())) {
            throw new FriendsException(FriendsConstant.NOT_ALLOWED_ADD_SELF);
        }
        // 这里也可以写动态sql，因为前面写过根据用户名和昵称查询了可以直接调用，我这里就直接分情况了
        UserFriends userFriends = null; // 这里可能出现空指针，就是当用户名/昵称没有传过来的情况，这个在下面考虑一下
        UserFriends userFriends2 = null;
        // 根据用户名添加
        if (addFriendsDTO.getUsername() != null) {
            // 判断用户名是否存在
            User user1 = userMapper.selectByUsername(addFriendsDTO.getUsername());
            // 3：好友不存在不能进行好友的添加
            if (user1 == null) {
                throw new FriendsException(FriendsConstant.USERNAME_OR_NICKNAME_NOT_EXISTS);
            }
            // 2：当用户已注销都不能进行好友的添加
            if (user1.getDeleted() == 1) {
                throw new FriendsException(FriendsConstant.USER_ALREADY_DELETED);
            }
            // 4：好友已存在也不能添加
            List<UserFriends> userFriends3 = userMapper.selectUserByIdAndFriendId(id, user1.getId());
            if (userFriends3 != null && !userFriends3.isEmpty()) {
                throw new FriendsException(FriendsConstant.FRIEND_ALREADY_EXIST);
            }


            // 进行添加操作，两个userFriends对象，一个是好友的，一个是自己的。双向
            // 组装属性(逻辑删除字段默认，创建时间得拼一下(当然，自定义注解那里也可以再来个operationType))
            userFriends = UserFriends.builder()
                    .userId(id)
                    .friendId(user1.getId())
                    .createTime(LocalDateTime.now())
                    .build();
            userFriends2 = UserFriends.builder()
                    .userId(user1.getId())
                    .friendId(id)
                    .createTime(LocalDateTime.now())
                    .deleted(0)  // 修改一下逻辑删除字段
                    .build();
        }
        // 根据昵称添加
        else if (addFriendsDTO.getNickname() != null) {
            // 判断昵称是否存在
            User user2 = userMapper.selectByNickName(addFriendsDTO.getNickname(), 0L);
            // 3：好友不存在不能进行好友的添加
            if (user2 == null) {
                throw new FriendsException(FriendsConstant.USERNAME_OR_NICKNAME_NOT_EXISTS);
            }
            //  2：当用户已注销都不能进行好友的添加
            if (user2.getDeleted() == 1) {
                throw new FriendsException(FriendsConstant.USER_ALREADY_DELETED);
            }
            // 4：好友已存在也不能添加
            List<UserFriends> userFriends3 = userMapper.selectUserByIdAndFriendId(id, user2.getId());
            if (userFriends3 != null && !userFriends3.isEmpty()) {
                throw new FriendsException(FriendsConstant.FRIEND_ALREADY_EXIST);
            }
            // 进行添加操作，两个userFriends对象，一个是好友的，一个是自己的。双向
            // 组装属性(逻辑删除字段默认，创建时间得拼一下(当然，自定义注解那里也可以再来个operationType))
            userFriends = UserFriends.builder()
                    .userId(id)
                    .friendId(user2.getId())
                    .createTime(LocalDateTime.now())
                    .build();
            userFriends2 = UserFriends.builder()
                    .userId(user2.getId())
                    .friendId(id)
                    .createTime(LocalDateTime.now())
                    .deleted(0)  // 修改一下逻辑删除字段
                    .build();
        }
        if (userFriends != null) {
            friendsMapper.add(userFriends);
            friendsMapper.add(userFriends2);
        }
    }

    /**
     * @param id
     * @return void
     * @author AlbertZhang
     * @description 删除好友
     * @date 2023-12-10 23:53
     **/
    @Override
    @Transactional
    public void delete(Long id) {
        // 没有携带好友id过来
        if (id == null) {
            throw new FriendsException(FriendsConstant.FRIENDS_ID_IS_NULL);
        }
        // 从context里面拿自己的id
        Long userId = BaseContext.getCurrentId();

        // 组装数据自己的数据
        UserFriends userFriends = UserFriends.builder()
                .userId(userId)
                .friendId(id)
                .deletedTime(LocalDateTime.now())
                .build();

        // 查看当前好友是否已经删除
        List<UserFriends> userFriends2 = friendsMapper.selectByFriendId(userFriends);   // 查出来的是两条数据，双向好友
        for (UserFriends friends : userFriends2) {
            // 好友已删除
            if (friends.getDeleted() == 1) {
                throw new FriendsException(FriendsConstant.FRIEND_ALREADY_DELETED);

            }
        }
        // 组装好友的谁
        UserFriends userFriends1 = UserFriends.builder()
                .userId(userFriends.getFriendId())
                .friendId(userFriends.getUserId())
                .deletedTime(LocalDateTime.now())
                .build();
        // 把两个都删除,逻辑删除
        friendsMapper.update(userFriends);
        friendsMapper.update(userFriends1);


    }

    /**
     * @param userId
     * @param friendId
     * @return java.util.List<com.gobanggame.pojo.vo.UserFriendChatVO>
     * @author AlbertZhang
     * @description 查询好友之间的聊天记录
     * @date 2023-12-11 14:17
     **/
    @Override
    public List<UserFriendChatVO> messageList(Long userId, Long friendId) {
        // 这是一个多表查询，user表和user_messages表联查。查询20条记录
        return userMessagesMapper.listTwentyRecords(userId, friendId);
    }

    /**
     * @param id
     * @return com.gobanggame.pojo.vo.MessageResult
     * @author AlbertZhang
     * @description 获取当前用户所有未读的消息列表
     * @date 2023-12-11 15:58
     **/
    @Override
    public MessageResult getNotReadMessageCount(Long id) {
        // 首先查询数据库里面的所有信息的个数
        Integer count = userMessagesMapper.selectByUserId(id);

        // 然后查询每个好友对应的个数
        List<MessageResult.UserMessageResult> userMessageResultList = userMessagesMapper.selectManyCountsByUserId(id);
        // 组装一下
        return new MessageResult(count, userMessageResultList);
    }

    /**
     * @param userId
     * @param friendId
     * @return void
     * @author AlbertZhang
     * @description 修改与好友所有未读的信息
     * @date 2023-12-11 16:26
     **/
    @Override
    @Transactional
    public void updateAllNotReadByFriendId(Long userId, Long friendId) {
        userMessagesMapper.update(userId, friendId);
    }
}
