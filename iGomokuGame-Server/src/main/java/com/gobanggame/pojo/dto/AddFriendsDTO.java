package com.gobanggame.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-10 21:39
 * @description: 添加好友DTO
 **/
@Data
@Schema(description = "添加好友DTO")
@NoArgsConstructor
@AllArgsConstructor
public class AddFriendsDTO {
    @Schema(description = "用户_好友id")
    private Long id;
    @Schema(description = "要添加的好友的用户名")
    private String username;
    @Schema(description = "要添加的好友的昵称")
    private String nickname;
}
