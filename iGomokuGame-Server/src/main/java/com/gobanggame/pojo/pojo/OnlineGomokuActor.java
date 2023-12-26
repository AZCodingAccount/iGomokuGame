package com.gobanggame.pojo.pojo;

import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-14 12:23
 * @description: 在线五子棋存储用户信息的实体类
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlineGomokuActor {
    private String role;
    private Session session;

    public void closeSession() {
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
        } catch (IOException e) {
            // 处理异常，可能是日志记录或其他操作
            System.err.println("关闭 Session 时出错: " + e.getMessage());
        }
    }

}
