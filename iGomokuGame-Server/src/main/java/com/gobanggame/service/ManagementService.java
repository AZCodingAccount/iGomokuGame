package com.gobanggame.service;

import com.gobanggame.pojo.entity.Feedback;
import com.gobanggame.pojo.entity.User;

import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-18 11:18
 * @description: 管理模块service
 **/
public interface ManagementService {
    /**
     * @author AlbertZhang
     * @description 获取所有反馈列表
     * @date 2023-12-19 17:42
     * @param
     * @return java.util.List<com.gobanggame.pojo.entity.Feedback>
     **/
    List<Feedback> getFeedbackList();

    /**
     * @author AlbertZhang
     * @description 解决用户反馈
     * @date 2023-12-19 17:42
     * @param feedback
     * @return void
     **/
    void fixFeedback(Feedback feedback);

    /**
     * @author AlbertZhang
     * @description 新增反馈
     * @date 2023-12-19 17:48
     * @param feedback
     * @return void
     **/
    void addFeedback(Feedback feedback);

    /**
     * @param
     * @param user
     * @return java.util.List<com.gobanggame.pojo.entity.User>
     * @author AlbertZhang
     * @description 获取用户列表
     * @date 2023-12-19 20:56
     **/
    List<User> getUserList(User user);
}
