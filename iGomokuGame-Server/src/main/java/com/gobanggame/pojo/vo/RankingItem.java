package com.gobanggame.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "排行榜条目")
public class RankingItem {
    @Schema(description = "id", example = "1")
    private String id;

    @Schema(description = "用户昵称", example = "张狗蛋")
    private String nickname;

    @Schema(description = "用户图片URL", example = "https://picture1/1.png")
    private String imageUrl;

    @Schema(description = "用户等级", example = "胜天半子")
    private String userLevel;


    @Schema(description = "游戏胜利次数", example = "100")
    private int gameSuccessCounts;

    @Schema(description = "游戏失败次数", example = "2")
    private int gameFailCounts;

    @Schema(description = "游戏平局次数", example = "0")
    private int gameDeadHeatCounts;

    @Schema(description = "用户得分", example = "5800")
    private int userScore;
}