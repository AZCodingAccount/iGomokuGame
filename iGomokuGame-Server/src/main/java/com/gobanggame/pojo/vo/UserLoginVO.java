package com.gobanggame.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-09 22:02
 * @description: 用户登录完成后返回的VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户登录后的信息封装")
public class UserLoginVO {

    @Schema(description = "用户ID", example = "123")
    private Long id;

    @Schema(description = "用户名", example = "demo123456")
    private String username;

    @Schema(description = "昵称", example = "这是示例昵称")
    private String nickname;

    @Schema(description = "描述", example = "这是示例描述")
    private String description;

    @Schema(description = "用户等级", example = "胜天半子")
    private String userLevel;
    @Schema(description = "用户头像", example = "http://1.jpg")
    private String imageUrl;
    @Schema(description = "是否在线", example = "在线")
    private String online;
    @Schema(description = "上次在线时间", example = "2020-12-16 18:18")
    private LocalDateTime lastOnlineTime;

    @Schema(description = "排行榜列表")
    private List<RankingItem> rankingList;

    @Schema(description = "JWT令牌", example = "asabsabds.hjbhsjsa.saj182292282029.hshsuahsak")
    private String jwt;

}
