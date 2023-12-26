package com.gobanggame.utils;

import com.gobanggame.constant.AIGameInteractionConstant;
import com.gobanggame.pojo.pojo.Coordinate;
import com.gobanggame.pojo.dto.GameAIMessageDTO;
import com.gobanggame.pojo.vo.GameAIMessageVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 15:38
 * @description: AI走法工具类
 **/
@Slf4j
public class AIUtil {

    public static GameAIMessageVO getAIChoice(GameAIMessageDTO.Coordinate lastMove, Integer level, Byte[][] boardStates, Integer stepOrder) {
        log.info("AI开始计算下一步坐标，当前搜索深度为{}", level);
        // 调用构造方法传递过去
        CalChessBoard calChessBoard = new CalChessBoard(boardStates, level);
        Coordinate moves = new Coordinate(lastMove.getX(), lastMove.getY());

        // 首先判断一下当前棋局状态，对手赢了吗
        byte gameStatus = calChessBoard.isGameOver(moves);
        if (gameStatus == CalChessBoard.BLACK) {
            return GameAIMessageVO.builder().gameStatus(0).message(AIGameInteractionConstant.GAME_SUCCESS_PERSON).build();
        }
        // 判断当前是平局吗？
        if (isDeadHeat(boardStates)) {
            // 平局
            return GameAIMessageVO.builder().gameStatus(2).message(AIGameInteractionConstant.GAME_DEAD_HEAT).build();
        }


        Coordinate coordinate = new Coordinate(lastMove.getX(), lastMove.getY());  // 转换一下数据
        // 判断当前对玩家的局势
        String fourMsg = "";    // 记录活四的msg
        String threeMsg = "";     // 记录活三的msg
        int[] gameStatus1 = CalChessBoard.gameStatus(boardStates, coordinate, CalChessBoard.BLACK);
        if (gameStatus1[0] != 0) {
            // 玩家双活四或活四，除非AI胜利，否则返回这个message
            if (gameStatus1[0] > 1) {
                // 双活四
                fourMsg = AIGameInteractionConstant.OPEN_FOUR_PLAYER_TWICE;
            } else {
                // 活四
                fourMsg = AIGameInteractionConstant.OPEN_FOUR_PLAYER;
            }
        }
        if (gameStatus1[1] != 0) {
            // 双活三，双活四或者活四，否则返回这个messgae
            if (gameStatus1[1] > 1) {
                // 双活三
                threeMsg = AIGameInteractionConstant.OPEN_THREE_PLAYER_Twice;
            } else {
                // 活三
                threeMsg = AIGameInteractionConstant.OPEN_THREE_PLAYER;
            }
        }

        // AI工作的入口。work这个方法
        GameAIMessageVO work = calChessBoard.work();
        String message = work.getMessage(); // AI的message，有局势或者空串

        if (!fourMsg.isEmpty()) {   // 玩家活四或双活四
            if (!message.equals(AIGameInteractionConstant.GAME_SUCCESS_AI)) {
                work.setMessage(fourMsg);
            }
            return work;
        }
        if (!threeMsg.isEmpty()) {  // 玩家活三或双活三
            if (message.equals(AIGameInteractionConstant.GAME_SUCCESS_AI)
                    || message.equals(AIGameInteractionConstant.OPEN_FOUR_AI_Twice)
                    || message.equals(AIGameInteractionConstant.OPEN_FOUR_AI)) {
                return work;
            }
            work.setMessage(threeMsg);
        }

        // 最后判断一下当前步数，如果大于12步且没有互动消息发出了，每4步返回一次局势信息
        if ((stepOrder > 12 && stepOrder % 4 == 0) && message.isEmpty() && threeMsg.isEmpty()) {
            work.setMessage(AIGameInteractionConstant.BOARD_STATUS_UNCERTAIN);
        }

        return work;

    }

    /**
     * @param boardStates
     * @return boolean
     * @author AlbertZhang
     * @description 判断当前是不是平局
     * @date 2023-12-25 15:41
     **/
    private static boolean isDeadHeat(Byte[][] boardStates) {

        boolean flag = true;
        for (Byte[] boardState : boardStates) {
            for (Byte aByte : boardState) {
                if (aByte == 0) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

}
