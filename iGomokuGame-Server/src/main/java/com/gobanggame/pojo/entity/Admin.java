package com.gobanggame.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * @program: ExampleProgram
 * @author: ExampleAuthor
 * @create: 2023-12-17
 * @description: Admin表实体类
 **/
@Data
@Schema(description = "Admin表实体类")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Schema(description = "管理员id", example = "10001")
    private Long id;

    @Schema(description = "用户名", example = "username123")
    private String username;

    @Schema(description = "昵称", example = "nickname123")
    private String nickname;

    @Schema(description = "密码", example = "password123")
    private String password;

    @Schema(description = "头像url", example = "http://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "账号权限", example = "admin")
    private String type;

    @Schema(description = "当前员工账号状态", example = "true")
    private Boolean status;

    @Schema(description = "账号创建时间", example = "2023-12-17T14:33:00")
    private LocalDateTime createTime;
}
