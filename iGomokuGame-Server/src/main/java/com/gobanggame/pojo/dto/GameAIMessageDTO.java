package com.gobanggame.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 14:48
 * @description: 人机对战中返回数据的格式
 **/

@Data
@Schema(description = "棋局状态值对象")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameAIMessageDTO {

    @Schema(description = "当前玩家落子的坐标")
    private Coordinate playerMoves;

    @Schema(description = "当前轮数", example = "11")
    private Integer stepOrder;

    @Schema(description = "棋局难度（1-4代表难度等级）", example = "3")
    private Integer level;

    @Schema(description = "棋局ID", example = "100")
    private Long gameId;

    @Schema(description = "棋盘状态数组")
    private Byte[][] boardStates;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinate {
        @Schema(description = "x坐标", example = "10")
        private Byte x;

        @Schema(description = "y坐标", example = "11")
        private Byte y;
    }

}
