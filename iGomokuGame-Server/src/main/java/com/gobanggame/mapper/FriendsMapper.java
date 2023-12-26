package com.gobanggame.mapper;

import com.gobanggame.pojo.entity.UserFriends;
import com.gobanggame.pojo.vo.UserSelectByIdVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-10 21:14
 * @description: 好友mapper
 **/
@Mapper
public interface FriendsMapper {
    /**
     * @param id
     * @return java.util.List<com.gobanggame.pojo.vo.UserSelectByIdVO>
     * @author AlbertZhang
     * @description 根据用户id查询关联的好友数据
     * @date 2023-12-10 21:27
     **/
    List<UserSelectByIdVO> list(Long id);

    /**
     * @param
     * @return void
     * @author AlbertZhang
     * @description 添加好友
     * @date 2023-12-10 22:01
     **/
    @Insert("insert into user_friends(user_id, friend_id,  create_time) VALUES(#{userId},#{friendId},#{createTime})")
    void add(UserFriends userFriends);

    /**
     * @param userFriends
     * @return void
     * @author AlbertZhang
     * @description 删除好友
     * @date 2023-12-10 23:57
     **/
    @Update("update user_friends set deleted=1 ,deleted_time=#{deletedTime} where user_id=#{userId} and friend_id=#{friendId}")
    void update(UserFriends userFriends);

    /**
     * @param userFriends
     * @return com.gobanggame.pojo.entity.UserFriends
     * @author AlbertZhang
     * @description 根据好友id和自己id查询好友账号状态——查看好友是否已注销功能
     * @date 2023-12-11 12:47
     **/
    @Select("select * from user_friends where user_id=#{userId} and friend_id=#{friendId}")
    List<UserFriends> selectByFriendId(UserFriends userFriends);
}
