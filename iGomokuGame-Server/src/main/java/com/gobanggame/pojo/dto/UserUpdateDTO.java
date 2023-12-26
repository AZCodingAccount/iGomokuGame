package com.gobanggame.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息更新数据传输对象")
public class UserUpdateDTO {

    @Schema(description = "用户ID", example = "2")
    private Long id;

    @Schema(description = "用户名", example = "demo123")
    private String username;

    @Schema(description = "密码", example = "demo123")
    private String password;

    @Schema(description = "昵称", example = "更新的示例昵称")
    private String nickname;

    @Schema(description = "性别", example = "男")
    private String gender;

    @Schema(description = "年龄", example = "18")
    private Integer age;

    @Schema(description = "自我介绍", example = "这是一段更新的示例自我介绍")
    private String description;

    @Schema(description = "社交账号", example = "我的qq账号是111111111")
    private String socialAccount;

    @Schema(description = "头像URL", example = "https://jshdshjdc/1.png")
    private String imageUrl;
}
