package com.gobanggame.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-09 21:34
 * @description: User表实体类
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户实体类")
public class User {
    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "john_doe")
    private String username;

    @Schema(description = "密码", example = "123456")
    private String password;

    @Schema(description = "用户昵称", example = "Johnny")
    private String nickname;

    @Schema(description = "性别", example = "男")
    private String gender;

    @Schema(description = "年龄", example = "30")
    private Integer age;

    @Schema(description = "个人描述", example = "这个人很懒，还没有描述哦")
    private String description;

    @Schema(description = "社交账号", example = "john_doe_social")
    private String socialAccount;

    @Schema(description = "头像URL", example = "http://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "用户棋力等级", example = "胜天半子")
    private String userLevel;

    @Schema(description = "用户分数", example = "100")
    private Integer userScore;

    @Schema(description = "总棋局数", example = "50")
    private Integer gameTotalCounts;

    @Schema(description = "与人下的棋局数", example = "30")
    private Integer gamePersonCounts;

    @Schema(description = "与AI下的棋局数", example = "20")
    private Integer gameAiCounts;

    @Schema(description = "胜利棋局数", example = "25")
    private Integer gameSuccessCounts;

    @Schema(description = "失败棋局数", example = "20")
    private Integer gameFailCounts;

    @Schema(description = "平局棋局数", example = "5")
    private Integer gameDeadHeatCounts;

    @Schema(description = "在线状态", example = "在线")
    private String online;

    @Schema(description = "最后一次在线时间", example = "2023-03-15 12:00:00")
    private LocalDateTime lastOnlineTime;

    @Schema(description = "创建时间", example = "2023-03-15 12:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2023-03-15 12:00:00")
    private LocalDateTime updateTime;

    @Schema(description = "是否被删除（逻辑删除）", example = "0")
    private Integer deleted;
}
