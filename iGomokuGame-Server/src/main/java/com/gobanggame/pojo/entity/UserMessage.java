package com.gobanggame.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Schema(description = "消息实体类")
@AllArgsConstructor
public class UserMessage {

    @Schema(description = "主键，自增")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "好友id")
    private Long friendId;

    @Schema(description = "发送的消息")
    private String message;

    @Schema(description = "消息时间戳")
    private LocalDateTime messageTime;

    @Schema(description = "标识信息是否已读 0已读 1未读")
    private Integer readed;
}