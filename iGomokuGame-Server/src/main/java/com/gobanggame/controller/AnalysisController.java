package com.gobanggame.controller;

import com.gobanggame.mapper.WebsiteDayInfoMapper;
import com.gobanggame.mapper.WebsiteVisitorDetailsInfoMapper;
import com.gobanggame.pojo.entity.WebsiteVisitorDetailsInfo;
import com.gobanggame.pojo.pojo.OnlineGomokuActor;
import com.gobanggame.pojo.vo.GomokuGameCount;
import com.gobanggame.pojo.pojo.Result;
import com.gobanggame.pojo.vo.MapData;
import com.gobanggame.pojo.vo.ServerMonitorVO;
import com.gobanggame.pojo.vo.WebsiteInfoGame;
import com.gobanggame.service.AnalysisService;
import com.gobanggame.task.WebsocketSessionTask;
import com.gobanggame.utils.IpUtils;
import com.gobanggame.websocket.OnlineGomokuServer;
import com.gobanggame.websocket.WebSocketFriendsChatServer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-19 22:52
 * @description:用于后台数据分析的控制器
 **/
@RestController
@Slf4j
@Tag(name = "用于后台分析的相关接口")
@RequestMapping("/api")
public class AnalysisController {
    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private WebsiteDayInfoMapper websiteDayInfoMapper;
    @Autowired
    private WebsiteVisitorDetailsInfoMapper websiteVisitorDetailsInfoMapper;

    @Autowired
    private OnlineGomokuServer onlineGomokuServer;

    @Autowired
    private WebSocketFriendsChatServer webSocketFriendsChatServer;

    @Autowired
    private WebsocketSessionTask websocketSessionTask;

    /**
     * @param request
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Object>
     * @author AlbertZhang
     * @description 添加网站访客详细
     * @date 2023-12-19 23:38
     **/
    @PostMapping("/add/website/click")
    @Operation(summary = "获取点击网站的访客信息")
    public Result<Object> addWebsiteClick(HttpServletRequest request) {
        // 获取ip
        String ipAddr = IpUtils.getIpAddr(request);
        // 获取ip对应城市
        String cityInfoByFile = IpUtils.getCityInfoByFile(ipAddr);
        String province = null;
        // 取出省份
        if (cityInfoByFile != null) {
            province = cityInfoByFile.split("\\|")[2];
        }
        // 如果省份是0，那就赋值为内网ip
        if (Objects.equals(province, "0")) {
            province = "内网ip";
        }
        // 插入到数据库
        analysisService.WebsiteClick(ipAddr, province);
        return Result.success();
    }


    /**
     * @param
     * @return com.gobanggame.pojo.pojo.Result<java.util.Map < java.lang.String, java.util.Map < java.time.LocalDate, java.lang.Integer>>>
     * @author AlbertZhang
     * @description 获取网站五子棋棋局的信息（每天）
     * @date 2023-12-20 11:07
     **/
    @GetMapping("/analysis/game/counts")
    @Operation(summary = "获取网站五子棋棋局的相关信息（每天）")
    public Result<List<GomokuGameCount>> getGameCountsByDay() {
        log.info("获取每天的五子棋信息");
        List<GomokuGameCount> gomokuGameCounts = websiteDayInfoMapper.selectGameCount();
        return Result.success(gomokuGameCounts);
    }

    /**
     * @author AlbertZhang
     * @description 获取网站的点击量、访客数、新增用户数相关信息（每天）—可以跟前面那个获取棋局信息合成一个接口
     * @date 2023-12-25 14:35
     * @param
     * @return com.gobanggame.pojo.pojo.Result<java.util.List<com.gobanggame.pojo.vo.WebsiteInfoGame>>
     **/
    @GetMapping("/analysis/website/info")
    @Operation(summary = "获取每天的网站访客量等信息")
    public Result<List<WebsiteInfoGame>> getWebsiteInfo() {
        log.info("获取每天的五子棋信息");
        List<WebsiteInfoGame> websiteInfoGames = websiteDayInfoMapper.selectWebsiteInfo();
        return Result.success(websiteInfoGames);
    }

    /**
     * @author AlbertZhang
     * @description 获取网站访客信息地域数信息
     * @date 2023-12-25 14:36
     * @param
     * @return com.gobanggame.pojo.pojo.Result<java.util.List<com.gobanggame.pojo.vo.MapData>>
     **/
    @GetMapping("/analysis/visitor/count")
    @Operation(summary = "获取网站访客地域数")
    public Result<List<MapData>> getAreaData() {
        log.info("获取网站详细访客信息");
        List<MapData> websiteInfoDetails = websiteVisitorDetailsInfoMapper.selectWebsiteDetailsCount();
        return Result.success(websiteInfoDetails);
    }

    /**
     * @param
     * @return com.gobanggame.pojo.pojo.Result<java.util.List < com.gobanggame.pojo.entity.WebsiteVisitorDetailsInfo>>
     * @author AlbertZhang
     * @description 获取网站点击访客详细信息——每次获取20条
     * @date 2023-12-21 16:13
     **/
    @GetMapping("/analysis/visitor/details")
    @Operation(summary = "获取网站访客详细信息")
    public Result<List<WebsiteVisitorDetailsInfo>> getWebsiteVisitorDetails() {
        log.info("轮询，获取网站访客详细信息");
        List<WebsiteVisitorDetailsInfo> websiteVisitorDetailsInfoList = websiteVisitorDetailsInfoMapper.selectWebsiteDetails();
        // 处理数据，根据访问时间升序返回
        websiteVisitorDetailsInfoList.sort(Comparator.comparing(WebsiteVisitorDetailsInfo::getAccessTime));
        return Result.success(websiteVisitorDetailsInfoList);
    }

    /**
     * @author AlbertZhang
     * @description 获取一些网站指标——网站在线用户数、网站目前棋局数、网站因超时被关闭的棋局数
     * @date 2023-12-25 14:37
     * @param
     * @return com.gobanggame.pojo.pojo.Result<com.gobanggame.pojo.vo.ServerMonitorVO>
     **/
    @GetMapping("/analysis/server/monitor")
    @Operation(summary = "服务器性能监控")
    public Result<ServerMonitorVO> getServerData() {
        log.info("获取服务器相关指标");
        // 获取roomSessions和roomBoards
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, OnlineGomokuActor>> roomSessions = onlineGomokuServer.getRoomSessions();
        ConcurrentHashMap<Long, Session> userSessions = webSocketFriendsChatServer.getUserSessions();
        ServerMonitorVO serverMonitorVO = new ServerMonitorVO();
        serverMonitorVO.setRoomCount(roomSessions.size());  // 设置目前棋局房间数
        serverMonitorVO.setSessionCount(userSessions.size());   // 设置在线用户数
        AtomicLong closeRoomCount = websocketSessionTask.getCloseRoomCount();   // 获取已关闭房间数
        serverMonitorVO.setClosedRoomCount(closeRoomCount.intValue());  // 设置超时关闭房间数

        return Result.success(serverMonitorVO);
    }
}
