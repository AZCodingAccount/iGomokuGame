package com.gobanggame.service;

import com.gobanggame.pojo.dto.GameAIMessageDTO;
import com.gobanggame.pojo.vo.GameAIMessageVO;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 13:59
 * @description: 五子棋对战service
 **/
public interface GameService {

    /**
     * @author AlbertZhang
     * @description 添加用户跟ai对局的信息
     * @date 2023-12-12 14:27
     * @param userId
     * @return java.lang.Long
     **/
    Long addAiGame(Long userId);

    /**
     * @author AlbertZhang
     * @description 人机对战时棋局的状态传输
     * @date 2023-12-12 15:00
     * @param gameAIMessageDTO
     * @return com.gobanggame.pojo.vo.GameAIMessageVO
     **/
    GameAIMessageVO transferAiMessage(GameAIMessageDTO gameAIMessageDTO);
}
