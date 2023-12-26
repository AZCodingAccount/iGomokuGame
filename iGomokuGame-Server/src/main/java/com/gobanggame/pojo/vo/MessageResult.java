package com.gobanggame.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-11 15:54
 * @description: 获取未读消息的实体类
 **/
@Data
@AllArgsConstructor
@Schema(description = "获取未读消息的VO")
public class MessageResult {
    @Schema(description = "总的未读消息")
    private Integer totalCount;
    private List<UserMessageResult> userMessageResultList;

    @Data
    @Schema(description = "用户对应的每个好友的未读消息数")
    public static class UserMessageResult{
        @Schema(description = "用户id")
        private Long userId;
        @Schema(description = "好友id")
        private Long friendId;
        @Schema(description = "未读消息数")
        private Integer count;
    }
}
