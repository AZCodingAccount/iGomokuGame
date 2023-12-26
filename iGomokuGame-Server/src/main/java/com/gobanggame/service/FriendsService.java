package com.gobanggame.service;

import com.gobanggame.pojo.vo.MessageResult;
import com.gobanggame.pojo.dto.AddFriendsDTO;
import com.gobanggame.pojo.vo.UserFriendChatVO;
import com.gobanggame.pojo.vo.UserSelectByIdVO;

import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-10 21:15
 * @description: 好友Service
 **/
public interface FriendsService {

    /**
     * @author AlbertZhang
     * @description 查询所有好友
     * @date 2023-12-10 21:25
     * @param
     * @return java.util.List<com.gobanggame.pojo.vo.UserSelectByIdVO>
     **/
    List<UserSelectByIdVO> list();


    /**
     * @author AlbertZhang
     * @description 添加好友
     * @date 2023-12-10 21:41
     * @param addFriendsDTO
     * @return void
     **/
    void add(AddFriendsDTO addFriendsDTO);

    /**
     * @author AlbertZhang
     * @description 删除好友
     * @date 2023-12-10 23:53
     * @param id
     * @return void
     **/
    void delete(Long id);

    /**
     * @author AlbertZhang
     * @description 查询好友之间的聊天记录
     * @date 2023-12-11 14:17
     * @param userId
     * @param friendId
     * @return java.util.List<com.gobanggame.pojo.vo.UserFriendChatVO>
     **/
    List<UserFriendChatVO> messageList(Long userId, Long friendId);

    /**
     * @author AlbertZhang
     * @description 获取当前用户所有未读的消息列表
     * @date 2023-12-11 15:58
     * @param id
     * @return com.gobanggame.pojo.vo.MessageResult
     **/
    MessageResult getNotReadMessageCount(Long id);

    /**
     * @author AlbertZhang
     * @description 修改好友所有未读的信息
     * @date 2023-12-11 16:25
     * @param userId
     * @param friendId
     * @return void
     **/
    void updateAllNotReadByFriendId(Long userId, Long friendId);
}
