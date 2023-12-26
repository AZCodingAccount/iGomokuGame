package com.gobanggame.controller;

import com.gobanggame.constant.UserConstant;
import com.gobanggame.mapper.UserMapper;
import com.gobanggame.pojo.entity.User;
import com.gobanggame.pojo.pojo.Result;
import com.gobanggame.pojo.dto.UserRegisterDTO;
import com.gobanggame.pojo.dto.UserUpdateDTO;
import com.gobanggame.pojo.vo.UserLoginVO;
import com.gobanggame.pojo.vo.UserSelectByIdVO;
import com.gobanggame.service.UserService;
import com.gobanggame.utils.AliOssUtil;
import com.gobanggame.utils.BaseContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @program: gobang
 * @author: AlbertZhang
 * @create: 2023-12-09 17:54
 * @description: 用户控制器
 **/
@RestController
@RequestMapping(value = "/api/user")
@Slf4j
@Tag(name = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private UserMapper userMapper;


    /**
     * @param userRegisterDTO
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Object>
     * @author AlbertZhang
     * @description 用户注册
     * @date 2023-12-09 21:23
     **/
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Object> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册，注册详细信息为：{}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return Result.success();
    }

    /**
     * @param userLoginDTO
     * @return com.gobanggame.pojo.pojo.Result<com.gobanggame.pojo.vo.UserLoginVO>
     * @author AlbertZhang
     * @description 用户登录
     * @date 2023-12-09 22:06
     **/
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<UserLoginVO> login(@RequestBody UserRegisterDTO userLoginDTO) {
        log.info("用户登录，登录的详细信息为：{}", userLoginDTO);
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }

    /**
     * @param id
     * @return com.gobanggame.pojo.pojo.Result<com.gobanggame.pojo.vo.UserSelectByIdVO>
     * @author AlbertZhang
     * @description 根据id查询用户信息
     * @date 2023-12-09 23:14
     **/
    @GetMapping("/{id}")    // 这个设计问题，不需要传id，jwt中可以获取id
    @Operation(summary = "根据id查询")
    public Result<UserSelectByIdVO> getById(@Parameter(description = "用户id") @PathVariable Long id) {
        log.info("根据id查询用户信息，本次查询的用户id为：{}", id);
        UserSelectByIdVO userSelectByIdVO = userService.getById(id);
        return Result.success(userSelectByIdVO);
    }

    @PostMapping("/upload")
    @Operation(summary = "用户头像上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传，{}", file);
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            String objectName = UUID.randomUUID() + extension;
            String filePath = aliOssUtil.upload(file.getBytes(), objectName, originalFilename);
            // 将照片url更新到数据库
            Long userId = BaseContext.getCurrentId();
            userMapper.update(User.builder().imageUrl(filePath).id(userId).build());
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败", e);
        }
        return Result.error(UserConstant.UPLOAD_FAILED);
    }

    /**
     * @param userUpdateDTO
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Object>
     * @author AlbertZhang
     * @description 更新用户信息
     * @date 2023-12-09 23:22
     **/
    @PutMapping
    @Operation(summary = "用户更新信息")
    public Result<Object> update(@RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("用户进行更新信息，更新信息为11111111：{}", userUpdateDTO);
        userService.update(userUpdateDTO);
        return Result.success();
    }

    /**
     * @param id
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Object>
     * @author AlbertZhang
     * @description 根据id删除用户信息
     * @date 2023-12-09 23:40
     **/
    @DeleteMapping("/{id}") // 这里是设计问题，不需要传id，jwt里面可以获取id
    @Operation(summary = "用户注销")
    public Result<Object> deleteById(@Parameter(description = "用户id") @PathVariable Long id) {
        log.info("用户注销：注销id为{}", id);
        userService.deleteById(id);
        return Result.success();
    }

}
