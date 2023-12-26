package com.gobanggame.mapper;

import com.gobanggame.pojo.entity.GameHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 14:24
 * @description: 操作棋局历史信息表
 **/
@Mapper
public interface GameHistoryMapper {
    /**
     * @param gameHistory
     * @return void
     * @author AlbertZhang
     * @description 插入游戏历史表
     * @date 2023-12-12 14:42
     **/
    void insert(GameHistory gameHistory);

    /**
     * @param gameHistory
     * @return void
     * @author AlbertZhang
     * @description 更新棋局表状态
     * @date 2023-12-13 20:39
     **/
    @Update("update game_history set end_time=#{endTime} ,game_result=#{gameResult}")
    void update(GameHistory gameHistory);

    /**
     * @param roomId
     * @return com.gobanggame.pojo.entity.GameHistory
     * @author AlbertZhang
     * @description 根据房间号查询游戏历史，需要查询最近的一个——实现超时关闭棋局连接的功能
     * @date 2023-12-23 12:40
     **/
    @Select("select * from game_history where room_id=#{roomId} order by begin_time desc limit 1 ")
    GameHistory selectByRoomId(Long roomId);

}
