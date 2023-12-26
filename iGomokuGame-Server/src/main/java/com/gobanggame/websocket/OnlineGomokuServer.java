package com.gobanggame.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobanggame.config.CustomSpringConfigurator;
import com.gobanggame.constant.UserGameSuccessConstant;
import com.gobanggame.mapper.GameDetailsMapper;
import com.gobanggame.mapper.GameHistoryMapper;
import com.gobanggame.mapper.UserMapper;
import com.gobanggame.mapper.WebsiteDayInfoMapper;
import com.gobanggame.pojo.entity.User;
import com.gobanggame.pojo.entity.WebsiteDayInfo;
import com.gobanggame.pojo.pojo.OnlineGomokuActor;
import com.gobanggame.pojo.pojo.OnlineGomokuMessage;
import com.gobanggame.pojo.entity.GameDetails;
import com.gobanggame.pojo.entity.GameHistory;
import com.gobanggame.pojo.vo.GameActor;
import com.gobanggame.pojo.vo.UserSelectByIdVO;
import com.gobanggame.utils.BaseContext;
import com.gobanggame.utils.GomokuGameUtil;
import com.gobanggame.utils.UserLevelUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-14 12:19
 * @description: 在线五子棋Server，是用于处理房间对战的websocket连接
 **/
@ServerEndpoint(value = "/game/online/gomoku/{roomId}/{userId}", configurator = CustomSpringConfigurator.class)
@Slf4j
@Component
public class OnlineGomokuServer {


    @Autowired
    private GameHistoryMapper gameHistoryMapper;
    @Autowired
    private GameDetailsMapper gameDetailsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebsiteDayInfoMapper websiteDayInfoMapper;

    // 使用 ConcurrentHashMap 来存储房间id和房间所关联的用户信息
    private static ConcurrentHashMap<Long, ConcurrentHashMap<Long, OnlineGomokuActor>> roomSessions = new ConcurrentHashMap<>();
    // 存储每个房间对应的棋盘
    private static ConcurrentHashMap<Long, Integer[][]> roomBoards = new ConcurrentHashMap<>();


    // 暴露出去供服务器性能监控模块访问
    public ConcurrentHashMap<Long, ConcurrentHashMap<Long, OnlineGomokuActor>> getRoomSessions() {
        return roomSessions;
    }

    public ConcurrentHashMap<Long, Integer[][]> getRoomBoards() {
        return roomBoards;
    }

    private ObjectMapper objectMapper = new ObjectMapper(); // Jackson的对象映射器

    // private static AtomicLong sessionCount = new AtomicLong(0); // 使用原子操作避免多线程的并发问题


    /**
     * @param session
     * @param roomId
     * @return void
     * @author AlbertZhang
     * @description 连接建立时需要做的事情，根据当前房间的人数，分三种情况处理
     * @date 2023-12-14 16:04
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") Long roomId, @PathParam("userId") Long userId) {
        // 1：当房间没有用户时，初始化房间
        if (roomSessions.get(roomId) == null || roomSessions.get(roomId).isEmpty()) {
            ConcurrentHashMap<Long, OnlineGomokuActor> roomUsers = new ConcurrentHashMap<>();
            Integer[][] board = new Integer[15][15];
            for (int i = 0; i < board.length; i++) {
                // 将每个位置设置为0
                Arrays.fill(board[i], 0);
            }
            roomBoards.put(roomId, board); // 存储初始化后的棋盘
            roomUsers.put(userId, new OnlineGomokuActor("执黑棋者", session));  // 放入当前用户
            roomSessions.put(roomId, roomUsers);    // 向roomSession中初始化房间对应的roomUsers
            log.info("初始化房间，当前房间id为：{}，建立房间的用户{}执黑子", roomId, userId);
        }
        //:2：当房间内有一个用户时，把当前用户加入到集合中
        else if (roomSessions.get(roomId).size() == 1) {
            roomSessions.get(roomId).put(userId, new OnlineGomokuActor("执白棋者", session));
            log.info("当前房间id为：{}，加入房间的用户为{}执白子", roomId, userId);
            // 添加棋局
            ConcurrentHashMap<Long, OnlineGomokuActor> map = roomSessions.get(roomId);
            Enumeration<Long> keys = map.keys();
            Long blackId = 0L;
            if (keys.hasMoreElements()) {
                blackId = keys.nextElement();  // 获取唯一的键
            }
            Session blackSession = map.get(blackId).getSession();
            GameHistory gameHistory = GameHistory.builder()
                    .roomId(roomId)
                    .whiteId(userId)
                    .blackId(blackId)
                    .beginTime(LocalDateTime.now())
                    .build();
            gameHistoryMapper.insert(gameHistory);

            // 维护网站day表
            WebsiteDayInfo websiteDayInfo = websiteDayInfoMapper.selectByDate(LocalDate.now());
            WebsiteDayInfo websiteDayInfo1 = WebsiteDayInfo.builder()
                    .aiGameCounts(websiteDayInfo.getHumanGameCount() + 1)
                    .recordDate(LocalDate.now())
                    .build();
            websiteDayInfoMapper.update(websiteDayInfo1);

            sendUserActorMessage(session, "执白棋者", gameHistory.getId());   // 向前端返回当前用户的角色
            sendUserActorMessage(blackSession, "执黑棋者", gameHistory.getId());   // 向前端返回黑棋用户的角色
            sendToAllUserForRoomCount(roomId);
        }
        //:3：当房间内超过两个用户时，向前端返回消息，询问是否要继续添加？
        else {
            sendMessageForConfirm(session);
        }
    }




    /**
     * @param session
     * @param roomId
     * @param message
     * @return void
     * @author AlbertZhang
     * @description 针对前端发送消息的不同类型进行处理，一共有1-5种类型
     * @date 2023-12-14 16:05
     **/
    @OnMessage
    public void onMessage(Session session, @PathParam("roomId") Long roomId, @PathParam("userId") Long userId, String message) throws JsonProcessingException {

        // 解析json数据
        // 解析消息来确定type，
        OnlineGomokuMessage onlineGomokuMessage = objectMapper.readValue(message, OnlineGomokuMessage.class);
        Integer type = onlineGomokuMessage.getType();

        if (type == 1) {    // 1：确认进入，添加用户到room集合中
            roomSessions.get(roomId).put(userId, new OnlineGomokuActor("观战者", session));
            sendToAllUserForRoomCount(roomId);
            sendToAllUserForObserver(roomId, userId, session);
            log.info("当前房间id为：{}，加入房间的用户为{}是观战者", roomId, userId);
        } else if (type == 3) {   // 2：聊天消息，广播给所有用户（意味着点击发送按钮时前端先不用渲染）
            sendToAllUser(3, roomId, onlineGomokuMessage.getMessage(), userId, onlineGomokuMessage.getRole(), 0);
        } else if (type == 5) {   //3：互动消息，跟聊天消息类似，主要是前端处理的不一样
            sendToAllUser(5, roomId, onlineGomokuMessage.getMessage(), userId, onlineGomokuMessage.getRole(), 0);
        } else if (type == 4) {   //4: 重头戏，要做三件事情。1：判断棋局状态并把消息广播出去。 2：维护game_details表。
            // 3：结束了以后维护game_history和user表并移除所有session

            Integer[][] board = roomBoards.get(roomId);// 获取这个房间对应的棋盘
            // 更新棋盘状态
            String steps = onlineGomokuMessage.getMessage();
            // System.out.println("步数" + steps);
            // 对字符串处理拿到x和y坐标
            String cleanedSteps = steps.replaceAll("[()]", ""); // 去除括号
            String[] split = cleanedSteps.split(","); // 根据逗号分割
            int color = Objects.equals(onlineGomokuMessage.getRole(), "执黑棋者") ? 1 : 2;// 获取颜色
            board[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = color;// 更新棋局的状态
            int gameOver = GomokuGameUtil.isGameOver(board);
            // 1：判断棋局状态并把消息广播出去
            if (gameOver == 0) {    // 游戏还在进行中，直接广播出去
                sendToAllUser(4, roomId, onlineGomokuMessage.getMessage(), userId, onlineGomokuMessage.getRole(), gameOver);
            } else {    // 游戏已经结束了
                UserSelectByIdVO userSelectByIdVO = userMapper.selectUserById(userId);
                if (gameOver == 1) {   // 黑色棋子胜利
                    GameHistory gameHistory = GameHistory.builder()
                            .endTime(LocalDateTime.now())
                            .gameResult(0)
                            .build();
                    gameHistoryMapper.update(gameHistory);
                    sendToAllUser(4, roomId, onlineGomokuMessage.getMessage(), userId, onlineGomokuMessage.getRole(), gameOver);
                    // 3：更新用户表
                    // 更新胜利的那一方
                    User user = User.builder()
                            .userScore(userSelectByIdVO.getUserScore() + UserGameSuccessConstant.PERSON_GAME_SUCCESS)
                            .gameTotalCounts(userSelectByIdVO.getGameTotalCounts() + 1)
                            .gameSuccessCounts(userSelectByIdVO.getGameSuccessCounts() + 1)
                            .gamePersonCounts(userSelectByIdVO.getGamePersonCounts() + 1)
                            .userLevel(UserLevelUtil.getUserLevelByUserScore(userSelectByIdVO.getUserScore() + UserGameSuccessConstant.PERSON_GAME_SUCCESS))
                            .id(userId)
                            .build();
                    userMapper.update(user);
                    // 更新失败的那一方
                    // 获取失败的用户id
                    ConcurrentHashMap<Long, OnlineGomokuActor> actors = roomSessions.get(roomId);
                    Long defeatUserId = 0L;
                    for (Long userId1 : actors.keySet()) {
                        OnlineGomokuActor actor = actors.get(userId1);
                        if (Objects.equals(actor.getRole(), "执白棋者")) {
                            defeatUserId = userId1;
                        }
                    }
                    User user1 = User.builder()
                            .userScore(userSelectByIdVO.getUserScore() + UserGameSuccessConstant.PERSON_GAME_FAIL)
                            .gameTotalCounts(userSelectByIdVO.getGameTotalCounts() + 1)
                            .gameFailCounts(userSelectByIdVO.getGameSuccessCounts() + 1)
                            .gamePersonCounts(userSelectByIdVO.getGamePersonCounts() + 1)
                            .userLevel(UserLevelUtil.getUserLevelByUserScore(userSelectByIdVO.getUserScore() + UserGameSuccessConstant.PERSON_GAME_FAIL))
                            .id(defeatUserId)
                            .build();
                    userMapper.update(user1);

                } else if (gameOver == 2) {   // 白色棋子胜利
                    GameHistory gameHistory = GameHistory.builder()
                            .endTime(LocalDateTime.now())
                            .gameResult(1)
                            .build();
                    gameHistoryMapper.update(gameHistory);
                    // 更新用户表
                    // 更新胜利的那一方
                    User user = User.builder()
                            .userScore(userSelectByIdVO.getUserScore() + UserGameSuccessConstant.PERSON_GAME_SUCCESS)
                            .gameTotalCounts(userSelectByIdVO.getGameTotalCounts() + 1)
                            .gameSuccessCounts(userSelectByIdVO.getGameSuccessCounts() + 1)
                            .gamePersonCounts(userSelectByIdVO.getGamePersonCounts() + 1)
                            .userLevel(UserLevelUtil.getUserLevelByUserScore(userSelectByIdVO.getUserScore() + UserGameSuccessConstant.PERSON_GAME_SUCCESS))
                            .id(userId)
                            .build();
                    userMapper.update(user);
                    // 更新失败的那一方
                    // 获取失败的用户id
                    ConcurrentHashMap<Long, OnlineGomokuActor> actors = roomSessions.get(roomId);
                    Long defeatUserId = 0L;
                    for (Long userId1 : actors.keySet()) {
                        OnlineGomokuActor actor = actors.get(userId1);
                        if (Objects.equals(actor.getRole(), "执黑棋者")) {
                            defeatUserId = userId1;
                        }
                    }
                    User user1 = User.builder()
                            .userScore(userSelectByIdVO.getUserScore() + UserGameSuccessConstant.PERSON_GAME_FAIL)
                            .gameTotalCounts(userSelectByIdVO.getGameTotalCounts() + 1)
                            .gameFailCounts(userSelectByIdVO.getGameSuccessCounts() + 1)
                            .gamePersonCounts(userSelectByIdVO.getGamePersonCounts() + 1)
                            .userLevel(UserLevelUtil.getUserLevelByUserScore(userSelectByIdVO.getUserScore() + UserGameSuccessConstant.PERSON_GAME_FAIL))
                            .id(defeatUserId)
                            .build();
                    userMapper.update(user1);


                    sendToAllUser(4, roomId, onlineGomokuMessage.getMessage(), userId, onlineGomokuMessage.getRole(), gameOver);
                } else if (gameOver == 3) { // 平局
                    GameHistory gameHistory = GameHistory.builder()
                            .endTime(LocalDateTime.now())
                            .gameResult(2)
                            .build();
                    gameHistoryMapper.update(gameHistory);

                    sendToAllUser(4, roomId, onlineGomokuMessage.getMessage(), userId, onlineGomokuMessage.getRole(), gameOver);
                    // 更新用户表
                    ConcurrentHashMap<Long, OnlineGomokuActor> actors = roomSessions.get(roomId);
                    for (Long userId1 : actors.keySet()) {
                        OnlineGomokuActor actor = actors.get(userId1);
                        if (!Objects.equals(actor.getRole(), "观战者")) {
                            User user1 = User.builder()
                                    .userScore(userSelectByIdVO.getUserScore() + UserGameSuccessConstant.PERSON_GAME_DEAD_HEAT)
                                    .gameTotalCounts(userSelectByIdVO.getGameTotalCounts() + 1)
                                    .gameDeadHeatCounts(userSelectByIdVO.getGameSuccessCounts() + 1)
                                    .gamePersonCounts(userSelectByIdVO.getGamePersonCounts() + 1)
                                    .userLevel(UserLevelUtil.getUserLevelByUserScore(userSelectByIdVO.getUserScore() + UserGameSuccessConstant.PERSON_GAME_FAIL))
                                    .id(userId1)
                                    .build();
                            userMapper.update(user1);
                        }
                    }

                }


                // 3：游戏结束，移除这个房间中的所有session和棋盘
                ConcurrentHashMap<Long, OnlineGomokuActor> room = roomSessions.get(roomId);
                roomBoards.remove(roomId); // 清除棋盘
                if (room != null) {
                    room.forEach((key, actor) -> {
                        actor.closeSession(); // 关闭连接
                    });
                    room.clear();   // 清除map中的session和其他信息
                }

            }

            // 2：维护game_details表。游戏结没结束把这个数据插入到数据库
            GameDetails gameDetails = GameDetails.builder()
                    .gameId(onlineGomokuMessage.getGameId())
                    .moveX(Byte.valueOf(split[0]))
                    .moveY(Byte.valueOf(split[1]))
                    .color((byte) color)
                    .stepOrder(onlineGomokuMessage.getStepOrder())
                    .build();
            gameDetailsMapper.insert(gameDetails);
        }


    }


    /**
     * @param session
     * @param roomId
     * @return void
     * @author AlbertZhang
     * @description 前端主动断开连接的逻辑处理，分两种情况，一个是观战者断开，一个是对战者断开
     * @date 2023-12-14 17:36
     **/
    @OnClose
    public void onClose(Session session, @PathParam("roomId") Long roomId, @PathParam("userId") Long userId) throws IOException {
        log.info("房间{}退出了用户{}", roomId, userId);
        // 判断当前退出的是不是对战者
        ConcurrentHashMap<Long, OnlineGomokuActor> map = roomSessions.get(roomId);
        String role = map.get(userId).getRole();
        if (!Objects.equals(role, "观战者")) {
            // 这样直接移除所有session
            // 移除这个房间中的所有session和棋盘
            ConcurrentHashMap<Long, OnlineGomokuActor> room = roomSessions.get(roomId);
            roomBoards.remove(roomId); // 清除棋盘
            if (room != null) {
                room.forEach((key, actor) -> {
                    actor.closeSession(); // 关闭连接
                });
                room.clear();   // 清除map中的session和其他信息
            }
            roomSessions.remove(roomId);    // 从map中移除
        } else {
            map.remove(userId);   // 移除当前这个用户信息
            sendToAllUserForRoomCount(roomId);
        }
    }

    /**
     * @author AlbertZhang
     * @description 发生了错误，打印一下日志即可
     * @date 2023-12-25 21:00
     * @param roomId 房间号
     * @param userId 发生错误的用户id
     * @param throwable 错误详细信息对象
     * @return void
     **/
    @OnError
    public void onError(@PathParam("roomId") Long roomId, @PathParam("userId") Long userId, Throwable throwable) {
        log.error("房间{}，用户{}的连接发生错误", roomId, userId, throwable);
    }




    /**
     * @return void
     * @author AlbertZhang
     * @description 这个方法用于广播房间人数的变化，当有新用户加入进来时，会调用这个方法广播房间人数的所有相关信息
     * @date 2023-12-14 16:13
     **/
    private void sendToAllUserForRoomCount(Long roomId) {
        // 获取当前房间的所有session
        ConcurrentHashMap<Long, OnlineGomokuActor> sessionsMap = roomSessions.get(roomId);
        log.info("广播人数变化{}", roomId);

        Enumeration<Long> keys = sessionsMap.keys();
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        List<GameActor> list = Collections.synchronizedList(new ArrayList<>());
        // 遍历集合
        while (keys.hasMoreElements()) {
            Long userId = keys.nextElement();
            // 在这里处理每个key
            // 根据id查询用户数据
            UserSelectByIdVO userSelectByIdVO = userMapper.selectUserById(userId);
            GameActor gameActor = new GameActor();
            BeanUtils.copyProperties(userSelectByIdVO, gameActor);
            OnlineGomokuActor actor = sessionsMap.get(userId);
            gameActor.setRole(actor.getRole());
            list.add(gameActor);
        }
        map.put("type", 6);
        map.put("actors", list);
        try {
            String jsonText = objectMapper.writeValueAsString(map);
            System.out.println("广播人数变化的文本：" + jsonText);
            // 转换成json
            keys = sessionsMap.keys();
            while (keys.hasMoreElements()) {
                Long userId = keys.nextElement();
                OnlineGomokuActor actor = sessionsMap.get(userId);
                if (actor != null) {
                    log.info("向用户广播：{}", userId);
                    actor.getSession().getBasicRemote().sendText(jsonText);
                }
            }
        } catch (Exception e) {
            log.error("广播人数变化时出现了异常:{}", e.getMessage());
        }


    }


    // 该方法用于返回用户的角色—在建立连接时发送给用户他应该是的角色
    private void sendUserActorMessage(Session session, String role, Long gameId) {
        if (session != null && session.isOpen()) {
            HashMap<Object, Object> map = new HashMap<>();
            map.put("type", 2);
            map.put("role", role);
            map.put("gameId", gameId);
            try {
                String jsonMessage = objectMapper.writeValueAsString(map);
                session.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                log.error("向用户发送警告信息时出现了异常");
            }
        }
    }


    /*
     *
     * 给新进来的观战者把当前棋盘状态发一下。
     * */
    private void sendToAllUserForObserver(Long roomId, Long userId, Session session) {

        try {
            // 只给新加的观战者发
            // 组装数据
            HashMap<Object, Object> map = new HashMap<>();
            map.put("type", 7);
            map.put("id", userId);
            map.put("role", "观战者");
            map.put("message", roomBoards.get(roomId));
            int gameOver = GomokuGameUtil.isGameOver(roomBoards.get(roomId));
            map.put("isGameOver", gameOver);
            String jsonMessage = objectMapper.writeValueAsString(map);
            // 服务器向客户端发送消息
            session.getBasicRemote().sendText(jsonMessage);
        } catch (Exception e) {
            log.error("传输数据发生异常");
        }


    }

    // 该方法用于提示用户是否还要继续加入房间——房间人数大于2的时候
    private void sendMessageForConfirm(Session session) {
        if (session != null && session.isOpen()) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("type", 0);
            try {
                String jsonMessage = objectMapper.writeValueAsString(map);
                session.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                log.error("向用户发送警告信息时出现了异常");
            }
        }

    }




    /**
     * @author AlbertZhang
     * @description 给所有用户广播数据——下棋广播，聊天广播
     * @date 2023-12-25 21:01
     * @param type 数据传输类型，约定了7种类型
     * @param roomId 房间id
     * @param message 传输消息内容
     * @param userId 传输用户
     * @param role 发消息的角色，观战者、执黑棋者、执白棋者
     * @param isGameOver 游戏是否结束
     * @return void
     **/
    public void sendToAllUser(Integer type, Long roomId, String message, Long userId, String role, Integer isGameOver) {
        // 获取当前房间的所有session
        ConcurrentHashMap<Long, OnlineGomokuActor> sessionsMap = roomSessions.get(roomId);
        Collection<OnlineGomokuActor> sessionUsers = sessionsMap.values();   // 获取所有值
        for (OnlineGomokuActor session : sessionUsers) {
            try {
                // 组装数据
                HashMap<Object, Object> map = new HashMap<>();
                map.put("type", type);
                map.put("id", userId);
                map.put("role", role);
                map.put("message", message);
                map.put("isGameOver", isGameOver);
                String jsonMessage = objectMapper.writeValueAsString(map);
                // 服务器向客户端发送消息
                session.getSession().getBasicRemote().sendText(jsonMessage);
            } catch (Exception e) {
                log.error("传输数据发生异常");
            }
        }
    }
}

