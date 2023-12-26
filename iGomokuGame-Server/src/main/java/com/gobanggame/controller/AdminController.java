package com.gobanggame.controller;

import com.gobanggame.mapper.AdminMapper;
import com.gobanggame.pojo.entity.Admin;
import com.gobanggame.pojo.pojo.JwtProperties;
import com.gobanggame.pojo.pojo.Result;
import com.gobanggame.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-20 18:33
 * @description: 管理员模块控制器
 **/
@RestController
@Slf4j
@Tag(name = "管理员相关接口")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private AdminMapper adminMapper;

    /**
     * @param admin
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Object>
     * @author AlbertZhang
     * @description 管理员进行登录
     * @date 2023-12-20 21:07
     **/
    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public Result<Object> login(@RequestBody Admin admin) {
        log.info("管理员登录：{}", admin);
        // 这里由于目前项目只提供登录，因此直接就在这里操作相关逻辑了。后续可以扩展
        // 查询用户是否存在
        Admin admins = adminMapper.login(admin);
        if (admins == null) {
            return Result.error("用户名或密码错误~");
        }

        // 生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", admins.getId());
        claims.put("userRole", "admin"); // 标识用户或管理员
        String jwt = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims);
        return Result.success(jwt);
    }
}
