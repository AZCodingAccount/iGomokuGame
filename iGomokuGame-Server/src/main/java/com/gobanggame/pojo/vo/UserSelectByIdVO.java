package com.gobanggame.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-09 23:02
 * @description: 用户回显数据实体类
 **/
@Data
@Schema(description = "根据id查询用户返回的VO")
public class UserSelectByIdVO {

    @Schema(description = "用户ID", example = "2")
    private long id;

    @Schema(description = "用户名", example = "demo123")
    private String username;

    @Schema(description = "昵称", example = "示例昵称")
    private String nickname;

    @Schema(description = "性别", example = "男")
    private String gender;

    @Schema(description = "年龄", example = "18")
    private int age;

    @Schema(description = "自我介绍", example = "这是一段示例自我介绍")
    private String description;

    @Schema(description = "社交账号", example = "我的qq账号是111111111")
    private String socialAccount;

    @Schema(description = "头像URL", example = "https://jshdshjdc/1.png")
    private String imageUrl;

    @Schema(description = "用户等级", example = "胜天半子")
    private String userLevel;

    @Schema(description = "用户得分", example = "5888")
    private int userScore;

    @Schema(description = "用户已下的总棋局数", example = "0")
    private int gameTotalCounts;

    @Schema(description = "用户跟人下的棋局数", example = "0")
    private int gamePersonCounts;

    @Schema(description = "用户跟人机下的棋局数", example = "0")
    private int gameAiCounts;

    @Schema(description = "用户胜利棋局数", example = "0")
    private int gameSuccessCounts;

    @Schema(description = "用户失败棋局数", example = "0")
    private int gameFailCounts;

    @Schema(description = "用户平局棋局数", example = "0")
    private int gameDeadHeatCounts;

    @Schema(description = "是否在线", example = "在线")
    private String online;
    @Schema(description = "上次在线时间", example = "2020-12-16 18:18")
    private LocalDateTime lastOnlineTime;

}
