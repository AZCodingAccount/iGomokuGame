package com.gobanggame.mapper;

import com.gobanggame.pojo.vo.MessageResult;
import com.gobanggame.pojo.entity.UserMessage;
import com.gobanggame.pojo.vo.UserFriendChatVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-11 00:05
 * @description: 消息数据库操作mapper
 **/
@Mapper
public interface UserMessagesMapper {
    /**
     * @param userMessage
     * @return void
     * @author AlbertZhang
     * @description 插入这条消息数据
     * @date 2023-12-11 0:09
     **/
    @Insert("insert into user_messages(user_id, friend_id, message, message_time,readed) " +
            "VALUES(#{userId},#{friendId},#{message},#{messageTime},#{readed})")
    void insert(UserMessage userMessage);

    /**
     * @param userId
     * @param friendId
     * @return java.util.List<com.gobanggame.pojo.vo.UserFriendChatVO>
     * @author AlbertZhang
     * @description 多表联查20条聊天数据——user表和user_messages表
     * @date 2023-12-11 14:20
     **/
    List<UserFriendChatVO> listTwentyRecords(Long userId, Long friendId);

    /**
     * @param id
     * @return java.lang.Long
     * @author AlbertZhang
     * @description 根据userId查询当前未读消息的个数
     * @date 2023-12-11 16:02
     **/
    @Select("select count(*) from user_messages where readed=1 and friend_id=#{id}")
    Integer selectByUserId(Long id);

    /**
     * @param id
     * @return java.util.List<com.gobanggame.pojo.vo.MessageResult.UserMessageResult>
     * @author AlbertZhang
     * @description 分组查询，根据不同friend进行一个聚合。查询与不同好友的未读消息数
     * @date 2023-12-11 16:08
     **/
    @Select("select count(*) as count,user_id as friendId,friend_id as userId from user_messages where friend_id=#{id} and readed=1 group by user_id")
    List<MessageResult.UserMessageResult> selectManyCountsByUserId(Long id);

    /**
     * @param userId
     * @param friendId
     * @return void
     * @author AlbertZhang
     * @description 修改好友所有未读的信息都为已读
     * @date 2023-12-11 16:26
     **/
    @Update("update user_messages set readed=0 where user_id=#{friendId} and friend_id=#{userId}")
    void update(Long userId, Long friendId);
}
