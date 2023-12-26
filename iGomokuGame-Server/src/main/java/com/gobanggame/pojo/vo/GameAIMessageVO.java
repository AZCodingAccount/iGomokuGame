package com.gobanggame.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 14:54
 * @description: 五子棋人机对战的数据传输DTO
 **/
@Data
@Schema(description = "AI消息数据传输对象")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameAIMessageVO {

    @Schema(description = "AI落子坐标")
    private Coordinate AIPieces;

    @Schema(description = "游戏状态（0用户胜利，1AI胜利，2平局，3棋局进行中）", example = "2")
    private Integer gameStatus;

    @Schema(description = "后端对棋局状态的判断发出的互动语句", example = "你居然活四了")
    private String message;

    @Schema(description = "AI思考时间（秒）", example = "10")
    private Long thinkTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinate {
        @Schema(description = "第x行", example = "10")
        private Byte x;

        @Schema(description = "第y列", example = "11")
        private Byte y;
    }
}
