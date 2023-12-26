package com.gobanggame.controller;

import com.gobanggame.pojo.pojo.Result;
import com.gobanggame.pojo.vo.GameAIMessageVO;
import com.gobanggame.pojo.dto.GameAIMessageDTO;
import com.gobanggame.service.GameService;
import com.gobanggame.utils.BaseContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 13:56
 * @description: 五子棋对局室的控制器（包括AI和用户）
 **/
@RestController
@RequestMapping("/api/game")
@Slf4j
@Tag(name = "棋盘对战相关接口")

public class GameController {
    @Autowired
    private GameService gameService;

    /**
     * @param
     * @return com.gobanggame.pojo.pojo.Result<java.lang.Long>
     * @author AlbertZhang
     * @description 添加用户与AI对局
     * @date 2023-12-12 14:26
     **/
    @PostMapping("/ai/games")
    @Operation(summary = "添加ai棋局")
    public Result<Long> addAiGame() {
        Long userId = BaseContext.getCurrentId();
        log.info("用户{}添加棋局", userId);
        Long gameId = gameService.addAiGame(userId);
        return Result.success(gameId);
    }

    /**
     * @param gameAIMessageDTO
     * @return com.gobanggame.pojo.pojo.Result<com.gobanggame.pojo.vo.GameAIMessageVO>
     * @author AlbertZhang
     * @description 人机对战时的数据传输
     * @date 2023-12-12 14:59
     **/
    @PostMapping("/ai/pieces")
    @Operation(summary = "ai棋局下棋数据传输")
    public Result<GameAIMessageVO> transferAiMessage(@RequestBody GameAIMessageDTO gameAIMessageDTO) {
        log.info("ai棋局下棋的数据传输，传输信息为：{}", gameAIMessageDTO);
        GameAIMessageVO gameAIMessageVO = gameService.transferAiMessage(gameAIMessageDTO);
        log.info("ai下棋数据传输返回，返回信息为{}", gameAIMessageVO);
        return Result.success(gameAIMessageVO);
    }


}
