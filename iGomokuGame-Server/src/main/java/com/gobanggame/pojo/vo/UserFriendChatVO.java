package com.gobanggame.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-11
 * @description: 用户好友聊天记录值对象
 **/
@Data
@Schema(description = "用户好友聊天记录值对象")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFriendChatVO {
    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户昵称", example = "User1Nick")
    private String nickname;

    @Schema(description = "用户头像URL", example = "http://sjdojdid/1.png")
    private String imageUrl;

    @Schema(description = "好友ID", example = "3")
    private Long friendId;

    @Schema(description = "消息内容", example = "你好")
    private String message;

    @Schema(description = "消息发送时间", example = "2023-12-11 12:37:40")
    private LocalDateTime messageTime;
}
