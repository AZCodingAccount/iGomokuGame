package com.gobanggame.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobanggame.config.CustomSpringConfigurator;
import com.gobanggame.mapper.UserMapper;
import com.gobanggame.mapper.UserMessagesMapper;
import com.gobanggame.pojo.entity.User;
import com.gobanggame.pojo.pojo.ChatMessage;
import com.gobanggame.pojo.entity.UserMessage;
import com.gobanggame.pojo.vo.UserSelectByIdVO;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/*
 * 这个session是一个进入这个网站就会建立session，在登录成功以后就会生成并维护。其主要功能有：
 * 1：聊天。 聊天时给出提示弹框       2：显示未读消息数。 3：在线功能的显示，忘了前端完成没完成了，反正后端逻辑是写好了
 * */
@ServerEndpoint(value = "/user/friend/{userId}", configurator = CustomSpringConfigurator.class)
@Slf4j
@Component
public class WebSocketFriendsChatServer {

    @Autowired
    private UserMessagesMapper userMessagesMapper;
    @Autowired
    private UserMapper userMapper;

    // 使用 ConcurrentHashMap 来存储用户ID和其对应的WebSocket Session
    private static ConcurrentHashMap<Long, Session> userSessions = new ConcurrentHashMap<>();

    // 供网站管理模块中服务器性能监控这部分使用
    public ConcurrentHashMap<Long, Session> getUserSessions() {
        return userSessions;
    }

    private ObjectMapper objectMapper = new ObjectMapper(); // Jackson的对象映射器

    private static AtomicLong sessionCount = new AtomicLong(0); // 使用原子操作避免多线程的并发问题


    // 打开的时候需要执行的操作，1：把用户session放入session集合中。2：更新用户的在线状态
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        long count = sessionCount.incrementAndGet();
        userSessions.put(userId, session);
        log.info("用户{}建立连接，当前连接数：{}", userId, count);
        // 更新在线状态
        User user = User.builder().online("在线").id(userId).build();
        userMapper.update(user);
    }

    // 接收到消息需要执行的操作，解析出来json数据，然后转发给另一个好友
    @OnMessage
    public void onMessage(@PathParam("userId") Long userId, String message) throws JsonProcessingException {
        // 解析json数据
        // 解析消息来确定目标用户，然后发送消息给目标用户
        ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
        long targetUserId = chatMessage.getTargetUserId();
        String msg = chatMessage.getMessage();
        log.info("用户{}要发送数据给{},信息为：{}", userId, targetUserId, msg);

        sendMessageToUser(userId, targetUserId, msg);
    }

    // 连接关闭时做的操作，1：从session集合中移除session。2：更新用户状态为不在线
    @OnClose
    public void onClose(@PathParam("userId") Long userId) {
        long count = sessionCount.decrementAndGet();
        log.info("用户{}的连接已关闭，还剩{}个连接", userId, count);
        userSessions.remove(userId);
        // 更新在线状态
        User user = User.builder().online("不在线").lastOnlineTime(LocalDateTime.now()).build();
        userMapper.update(user);
    }

    // 连接发生错误时执行的操作，打印日志
    @OnError
    public void onError(@PathParam("userId") Long userId, Throwable throwable) {
        log.error("用户{}的连接发生错误{}", userId, throwable.getMessage());
    }

    // 发送消息给特定的用户。分为两种情况。1：用户在线：把信息直接转发给他。  2：用户不在线，把信息存储到数据库
    public void sendMessageToUser(Long userId, Long targetUserId, String message) {
        System.out.println(targetUserId);
        Session targetSession = userSessions.get(targetUserId);
        if (targetSession != null && targetSession.isOpen()) {
            // 这里需要把message存储到user_messages这个表里面
            // 组装一下数据
            UserMessage userMessage = new UserMessage(null, userId, targetUserId, message, LocalDateTime.now(), 0);
            userMessagesMapper.insert(userMessage);
            try {
                // 根据userID查询出当前昵称
                UserSelectByIdVO userSelectByIdVO = userMapper.selectUserById(userId);
                String nickname = userSelectByIdVO.getNickname();
                // 组装json数据返回给前端
                ChatMessage chatMessage = new ChatMessage(userId, targetUserId, nickname, message);
                String jsonMessage = objectMapper.writeValueAsString(chatMessage);
                targetSession.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                log.error("向{}发送消息时出现了异常，异常信息为：{}", targetUserId, e.getMessage());
            }
        } else {    // 用户离线
            // 这里需要把message存储到user_messages这个表里面，并设置信息为未读1
            UserMessage userMessage = new UserMessage(null, userId, targetUserId, message, LocalDateTime.now(), 1);
            userMessagesMapper.insert(userMessage);
        }
    }
}
