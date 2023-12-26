package com.gobanggame.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-09 21:20
 * @description: 注册时使用的DTO
 **/
@Data
@Schema(description = "注册DTO")
public class UserRegisterDTO {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
}
