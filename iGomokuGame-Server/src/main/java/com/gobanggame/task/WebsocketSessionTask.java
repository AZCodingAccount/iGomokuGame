package com.gobanggame.task;

import com.gobanggame.mapper.GameHistoryMapper;
import com.gobanggame.pojo.entity.GameHistory;
import com.gobanggame.pojo.pojo.OnlineGomokuActor;
import com.gobanggame.websocket.OnlineGomokuServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-23 12:25
 * @description: websocketTask管理的定时任务类
 **/
@Component
@Slf4j
public class WebsocketSessionTask {

    @Autowired
    private OnlineGomokuServer onlineGomokuServer;

    @Autowired
    private GameHistoryMapper gameHistoryMapper;

    private static AtomicLong closeRoomCount = new AtomicLong(0); // 使用原子操作避免多线程的并发问题

    public AtomicLong getCloseRoomCount() {
        return closeRoomCount;
    }

    /**
     * @param
     * @return void
     * @author AlbertZhang
     * @description 移除超过一个小时的room
     * @date 2023-12-23 12:30
     **/
    @Scheduled(cron = "0 0 * * * ?")  // 每小时执行一次
    // @Scheduled(fixedRate = 10000)  // 每10秒执行一次
    public void removeSessions() {
        // 获取roomSessions和roomBoards
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, OnlineGomokuActor>> roomSessions = onlineGomokuServer.getRoomSessions();
        ConcurrentHashMap<Long, Integer[][]> roomBoards = onlineGomokuServer.getRoomBoards();

        // 遍历所有房间，查找他们已经进行了多久
        for (Map.Entry<Long, ConcurrentHashMap<Long, OnlineGomokuActor>> roomEntry : roomSessions.entrySet()) {
            Long roomId = roomEntry.getKey(); // 房间ID
            ConcurrentHashMap<Long, OnlineGomokuActor> userSessions = roomEntry.getValue();

            // 在这里执行校验逻辑
            GameHistory gameHistory = gameHistoryMapper.selectByRoomId(roomId);

            if (gameHistory != null) {
                // 如果说当前时间-他们的开始时间大于了一个小时，那我就关闭连接
                LocalDateTime beginTime = gameHistory.getBeginTime();
                // 检查当前时间与开始时间的差距
                if (Duration.between(beginTime, LocalDateTime.now()).toHours() > 1) {
                    long l = closeRoomCount.incrementAndGet();
                    log.info("房间{}超过时间限制，关闭所有会话,当前关闭的房间数有{}个", roomId, l);
                    // 如果时间差超过1小时，关闭所有相关会话
                    for (OnlineGomokuActor actor : userSessions.values()) {
                        try {
                            if (actor.getSession().isOpen()) {
                                actor.getSession().close();
                            }
                        } catch (IOException e) {
                            log.error("定时任务关闭用户会话时出现了异常“{}", e.getMessage());
                        }
                    }
                    // 移除会话
                    roomSessions.remove(roomId);
                    roomBoards.remove(roomId);
                }
            }
        }

    }


}
