package com.gobanggame.controller;

import com.gobanggame.pojo.entity.Feedback;
import com.gobanggame.pojo.entity.User;
import com.gobanggame.pojo.pojo.Result;
import com.gobanggame.service.ManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-17 18:06
 * @description: 管理后台Controller
 **/
@RestController
@Slf4j
@Tag(name = "后台管理的相关接口")
@RequestMapping("/api/management")
public class ManagementController {
    @Autowired
    private ManagementService managementService;

    /**
     * @param
     * @return com.gobanggame.pojo.pojo.Result<java.util.List < com.gobanggame.pojo.entity.Feedback>>
     * @author AlbertZhang
     * @description 获取所有反馈列表
     * @date 2023-12-18 11:22
     **/
    @GetMapping("/feedbacks")
    @Operation(summary = "获取反馈列表")
    public Result<List<Feedback>> getFeedbackList() {
        log.info("超级管理员获取反馈列表");
        List<Feedback> feedbackList = managementService.getFeedbackList();
        return Result.success(feedbackList);
    }

    /**
     * @param feedback
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Object>
     * @author AlbertZhang
     * @description 更新用户反馈信息
     * @date 2023-12-19 17:41
     **/
    @PutMapping("/feedbacks")
    @Operation(summary = "解决用户反馈")
    public Result<Object> fixedFeedback(@RequestBody Feedback feedback) {
        log.info("解决用户反馈，反馈信息为：{}", feedback);
        managementService.fixFeedback(feedback);
        return Result.success();
    }

    /**
     * @param feedback
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Object>
     * @author AlbertZhang
     * @description 新增反馈
     * @date 2023-12-19 17:48
     **/
    @PostMapping("/feedbacks")
    @Operation(summary = "新增反馈")
    public Result<Object> addFeedback(@RequestBody Feedback feedback) {
        log.info("新增反馈：{}", feedback);
        managementService.addFeedback(feedback);
        return Result.success();
    }

    /**
     * @author AlbertZhang
     * @description 获取用户列表
     * @date 2023-12-19 20:55
     * @param
     * @return com.gobanggame.pojo.pojo.Result<java.util.List<com.gobanggame.pojo.entity.User>>
     **/
    @GetMapping("/users")
    @Operation(summary = "获取用户列表")
    public Result<List<User>> getUserList(User user){
        log.info("获取用户列表");
        List<User> userList=managementService.getUserList(user);
        return Result.success(userList);
    }


}
