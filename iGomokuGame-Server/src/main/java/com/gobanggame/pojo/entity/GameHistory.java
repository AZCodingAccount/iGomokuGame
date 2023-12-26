package com.gobanggame.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 14:26
 * @description: GameHistory表实体类
 **/
@Data
@Schema(description = "GameHistory表实体类")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameHistory {

    @Schema(description = "主键非空，棋局id", example = "1001")
    private Long id;

    @Schema(description = "房间id", example = "2002")
    private Long roomId;

    @Schema(description = "黑棋用户id", example = "2002")
    private Long blackId;

    @Schema(description = "白棋用户id（0代表是AI对战）", example = "3003")
    private Long whiteId;

    @Schema(description = "游戏开始时间", example = "2023-12-10 14:00:00")
    private LocalDateTime beginTime;

    @Schema(description = "游戏结束时间", example = "2023-12-10 15:00:00")
    private LocalDateTime endTime;

    @Schema(description = "游戏结果（0黑胜，1白胜，2平）", example = "0")
    private Integer gameResult;

}
