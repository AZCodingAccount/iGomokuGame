package com.gobanggame.controller;

import com.gobanggame.pojo.vo.MessageResult;
import com.gobanggame.pojo.pojo.Result;
import com.gobanggame.pojo.dto.AddFriendsDTO;
import com.gobanggame.pojo.vo.UserFriendChatVO;
import com.gobanggame.pojo.vo.UserSelectByIdVO;
import com.gobanggame.service.FriendsService;
import com.gobanggame.utils.BaseContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-10 21:13
 * @description: 好友控制器
 **/
@RestController
@RequestMapping(value = "/api/user/friend")
@Slf4j
@Tag(name = "好友相关接口")
public class FriendsController {

    @Autowired
    private FriendsService friendsService;

    /**
     * @param
     * @return com.gobanggame.pojo.pojo.Result<java.util.List < com.gobanggame.pojo.vo.UserSelectByIdVO>>
     * @author AlbertZhang
     * @description 查询所有好友
     * @date 2023-12-10 21:25
     **/
    @GetMapping("/list")
    @Operation(summary = "查询所有好友")
    public Result<List<UserSelectByIdVO>> list() {
        log.info("查询所有好友");
        List<UserSelectByIdVO> userSelectByIdVOList = friendsService.list();
        return Result.success(userSelectByIdVOList);
    }

    /**
     * @param addFriendsDTO
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Object>
     * @author AlbertZhang
     * @description 添加好友
     * @date 2023-12-10 21:41
     **/
    @PostMapping
    @Operation(summary = "添加好友")
    public Result<Object> add(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "要添加好友的相关信息")
                              @RequestBody AddFriendsDTO addFriendsDTO) {
        log.info("添加好友，好友信息为：{}", addFriendsDTO);
        friendsService.add(addFriendsDTO);
        return Result.success();
    }

    /**
     * @param friendId
     * @return com.gobanggame.pojo.pojo.Result<java.util.List < com.gobanggame.pojo.vo.UserFriendChatVO>>
     * @author AlbertZhang
     * @description 查询好友的聊天记录
     * @date 2023-12-20 21:09
     **/
    @GetMapping("/list/{friendId}")
    @Operation(summary = "查询聊天记录")
    public Result<List<UserFriendChatVO>> messageList(@PathVariable Long friendId) {
        Long userId = BaseContext.getCurrentId();
        log.info("查询{}与好友{}的聊天记录", userId, friendId);
        List<UserFriendChatVO> chatMessageVOList = friendsService.messageList(userId, friendId);
        return Result.success(chatMessageVOList);
    }

    /**
     * @param id
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Object>
     * @author AlbertZhang
     * @description 删除好友
     * @date 2023-12-10 23:53
     **/
    @DeleteMapping("/{id}")
    @Operation(summary = "删除好友")
    public Result<Object> delete(@Parameter(description = "要删除的好友id") @PathVariable Long id) {
        log.info("要删除好友，好友id为：{}", id);
        friendsService.delete(id);
        return Result.success();
    }

    /**
     * @param
     * @return com.gobanggame.pojo.pojo.Result<com.gobanggame.pojo.vo.MessageResult>
     * @author AlbertZhang
     * @description 获取当前用户所有未读的消息列表
     * @date 2023-12-11 15:58
     **/
    @GetMapping("/unread/message/list")
    @Operation(summary = "获取用户所有未读的消息列表")
    public Result<MessageResult> getNotReadMessageCount() {
        Long id = BaseContext.getCurrentId();
        log.info("获取当前用户{}所有未读的消息列表", id);
        MessageResult messageResult = friendsService.getNotReadMessageCount(id);
        return Result.success(messageResult);
    }

    /**
     * @param friendId
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Object>
     * @author AlbertZhang
     * @description 修改好友所有未读的信息
     * @date 2023-12-11 16:25
     **/
    @PutMapping("/message/{friendId}")
    @Operation(summary = "更新好友所有未读的信息")
    public Result<Object> updateAllNotReadByFriendId(@Parameter(description = "好友id") @PathVariable Long friendId) {
        Long userId = BaseContext.getCurrentId();
        log.info("修改用户{}的好友{}所有消息都为已读", userId, friendId);
        friendsService.updateAllNotReadByFriendId(userId, friendId);
        return Result.success();
    }
}
