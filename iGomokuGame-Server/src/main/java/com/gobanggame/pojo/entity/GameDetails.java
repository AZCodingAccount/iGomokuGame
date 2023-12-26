package com.gobanggame.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 14-33
 * @description: GameDetails表实体类
 **/
@Data
@Schema(description = "GameDetails表实体类")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDetails {

    @Schema(description = "主键非空，历史信息id", example = "1001")
    private Long id;

    @Schema(description = "棋局id", example = "5005")
    private Long gameId;

    @Schema(description = "移动的x坐标", example = "7")
    private Byte moveX;

    @Schema(description = "移动的y坐标", example = "7")
    private Byte moveY;

    @Schema(description = "棋子颜色（0黑色，1白色）", example = "0")
    private Byte color;

    @Schema(description = "移动的顺序", example = "10")
    private Integer stepOrder;
}
