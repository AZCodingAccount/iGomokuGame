package com.gobanggame.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

import java.time.LocalDateTime;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-10 21:46
 * @description: user_friends表实体类
 **/
@Data
@Schema(description = "user_friends表实体类")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFriends {
    @Schema(description = "主键，自增")
    private Long id;

    @Schema(description = "用户id", example = "1")
    private Long userId;

    @Schema(description = "好友id", example = "3")
    private Long friendId;

    @Builder.Default
    @Schema(description = "逻辑删除字段", example = "1")
    private Integer deleted = 0;

    @Schema(description = "创建时间", example = "2023-03-15 12:00:00")
    private LocalDateTime createTime;

    @Schema(description = "删除时间", example = "2023-03-15 12:00:00")
    private LocalDateTime deletedTime;


}
