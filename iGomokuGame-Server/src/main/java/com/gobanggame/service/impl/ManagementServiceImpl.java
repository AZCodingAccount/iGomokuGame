package com.gobanggame.service.impl;

import com.gobanggame.mapper.FeedbackMapper;
import com.gobanggame.mapper.UserMapper;
import com.gobanggame.pojo.entity.Feedback;
import com.gobanggame.pojo.entity.User;
import com.gobanggame.service.ManagementService;
import com.gobanggame.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-18 11:18
 * @description: 后台中管理模块的实现类
 **/
@Service
public class ManagementServiceImpl implements ManagementService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * @param
     * @return java.util.List<com.gobanggame.pojo.entity.Feedback>
     * @author AlbertZhang
     * @description 获取所有反馈列表
     * @date 2023-12-18 11:22
     **/
    @Override
    public List<Feedback> getFeedbackList() {
        return feedbackMapper.list();
    }

    /**
     * @param feedback
     * @return void
     * @author AlbertZhang
     * @description 解决用户反馈
     * @date 2023-12-19 17:42
     **/
    @Override
    @Transactional
    public void fixFeedback(Feedback feedback) {
        // 主要是解决反馈，检查后台传过来的数据不为空，如果通过接口传的数据，不判断可能会报空指针
        if (feedback != null) {
            feedback.setFixedTime(LocalDateTime.now());
            feedbackMapper.update(feedback);
        }
    }

    /**
     * @param feedback
     * @return void
     * @author AlbertZhang
     * @description 新增反馈
     * @date 2023-12-19 17:48
     **/
    @Override
    public void addFeedback(Feedback feedback) {
        if (feedback != null) {
            Long userId = BaseContext.getCurrentId();
            feedback.setUserId(userId);
            feedback.setFeedbackTime(LocalDateTime.now());
            feedbackMapper.insert(feedback);
        }
    }

    /**
     * @param
     * @param user
     * @return java.util.List<com.gobanggame.pojo.entity.User>
     * @author AlbertZhang
     * @description 获取用户列表
     * @date 2023-12-19 20:56
     **/
    @Override
    public List<User> getUserList(User user) {
        return userMapper.list(user);
    }
}
