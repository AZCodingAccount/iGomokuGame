package com.gobanggame.mapper;

import com.gobanggame.pojo.entity.GameDetails;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 14:25
 * @description: 操作棋局历史信息细节表
 **/
@Mapper
public interface GameDetailsMapper {

    /**
     * @author AlbertZhang
     * @description 插入每一步的详细信息数据
     * @date 2023-12-12 15:23
     * @param gameDetails
     * @return void
     **/
    @Insert("insert into game_details(game_id, move_x, move_y, color, step_order) VALUES (#{gameId},#{moveX},#{moveY}," +
            "#{color},#{stepOrder})")
    void insert(GameDetails gameDetails);
}
