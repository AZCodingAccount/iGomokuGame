package com.gobanggame.service.impl;

import com.gobanggame.constant.UserGameSuccessConstant;
import com.gobanggame.mapper.GameDetailsMapper;
import com.gobanggame.mapper.GameHistoryMapper;
import com.gobanggame.mapper.UserMapper;
import com.gobanggame.mapper.WebsiteDayInfoMapper;
import com.gobanggame.pojo.dto.GameAIMessageDTO;
import com.gobanggame.pojo.entity.GameDetails;
import com.gobanggame.pojo.entity.GameHistory;
import com.gobanggame.pojo.entity.User;
import com.gobanggame.pojo.entity.WebsiteDayInfo;
import com.gobanggame.pojo.vo.GameAIMessageVO;
import com.gobanggame.pojo.vo.UserSelectByIdVO;
import com.gobanggame.service.GameService;
import com.gobanggame.utils.AIUtil;
import com.gobanggame.utils.BaseContext;
import com.gobanggame.utils.UserLevelUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 14:01
 * @description: 五子棋棋局室service实现类
 **/
@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameHistoryMapper gameHistoryMapper;
    @Autowired
    private GameDetailsMapper gameDetailsMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WebsiteDayInfoMapper websiteDayInfoMapper;

    /**
     * @param userId
     * @return java.lang.Long
     * @author AlbertZhang
     * @description 添加用户跟ai对局的历史信息
     * @date 2023-12-12 14:27
     **/
    @Override
    @Transactional
    public Long addAiGame(Long userId) {
        // 组装数据
        GameHistory gameHistory = GameHistory.builder()
                .blackId(userId)
                .whiteId(0L)
                .beginTime(LocalDateTime.now()).build();

        gameHistoryMapper.insert(gameHistory);

        // 维护website_day_info表。新增一个AI棋局对战
        WebsiteDayInfo websiteDayInfo = websiteDayInfoMapper.selectByDate(LocalDate.now());
        WebsiteDayInfo websiteDayInfo1 = WebsiteDayInfo.builder()
                .aiGameCounts(websiteDayInfo.getAiGameCounts() + 1)
                .recordDate(LocalDate.now())
                .build();
        websiteDayInfoMapper.update(websiteDayInfo1);
        return gameHistory.getId();
    }

    /**
     * @param gameAIMessageDTO
     * @return com.gobanggame.pojo.vo.GameAIMessageVO
     * @author AlbertZhang
     * @description 人机对战时棋局状态的传输
     * @date 2023-12-12 15:00
     **/
    @Override
    @Transactional
    public GameAIMessageVO transferAiMessage(GameAIMessageDTO gameAIMessageDTO) {
        /*
         * 在这个service里面我们要做四件事情
         *       1：首先将当前这一步棋存储到数据库中。
         *       2：然后调用AIUtil计算下一步AI的落子，在这个工具方法已经组装好了数据（计算时间在外部添加）
         *       3：gameStatus判断棋局状态，未结束的话把AI的棋也存到数据库里
         *       4：结束的话首先更新棋局状态（game_history表）更新 user表维护棋局数和用户等级
         * */
        // 1：将这步棋存储到数据库
        GameDetails gamePlayerDetails = GameDetails.builder()
                .gameId(gameAIMessageDTO.getGameId())
                .moveX(gameAIMessageDTO.getPlayerMoves().getX())
                .moveY(gameAIMessageDTO.getPlayerMoves().getY())
                .color((byte) 0)
                .stepOrder(gameAIMessageDTO.getStepOrder())
                .build();
        gameDetailsMapper.insert(gamePlayerDetails);
        LocalDateTime startTime = LocalDateTime.now();
        // 2：然后调用AIUtil计算下一步AI的落子，在这个工具方法已经组装好了数据（计算时间在外部添加）
        GameAIMessageVO gameAIMessageVO = AIUtil.getAIChoice(gameAIMessageDTO.getPlayerMoves(), gameAIMessageDTO.getLevel(), gameAIMessageDTO.getBoardStates(), gameAIMessageDTO.getStepOrder());
        LocalDateTime endTime = LocalDateTime.now();

        // 计算两个时间点之间的持续时间
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();

        // 设置思考时间
        gameAIMessageVO.setThinkTime(seconds);

        // 判断当前游戏状态，游戏结束后要修改game_history和user表
        if (gameAIMessageVO.getGameStatus() != 3) { // 当前游戏已经结束了 0 用户胜利 1 AI胜利 2 平局 3棋局进行中
            // 更新数据库中棋局历史状态。不需要添加AI的步数到数据库因为AI都没走
            GameHistory gameHistory = GameHistory.builder()
                    .endTime(LocalDateTime.now())
                    .gameResult(gameAIMessageVO.getGameStatus())
                    .build();

            gameHistoryMapper.update(gameHistory);

            // 更新user表，游戏结束了要给用户加分
            Long userId = BaseContext.getCurrentId();
            UserSelectByIdVO user = userMapper.selectUserById(userId);
            int userScore = user.getUserScore();
            // 获取用户局数
            int gameTotalCounts = user.getGameTotalCounts();
            int gameAiCounts = user.getGameAiCounts();
            int gameSuccessCounts = user.getGameSuccessCounts();
            int gameFailCounts = user.getGameFailCounts();
            int gameDeadHeatCounts = user.getGameDeadHeatCounts();

            // 更新userScore并判断他的userLevel需不需要更新，并更新局数
            if (gameAIMessageVO.getGameStatus() == 0) { // 用户胜利
                userScore += UserGameSuccessConstant.AI_GAME_SUCCESS;
                gameSuccessCounts += 1;
            } else if (gameAIMessageVO.getGameStatus() == 1) {  // 用户失败
                userScore += UserGameSuccessConstant.AI_GAME_FAIL;
                gameFailCounts += 1;
            } else if (gameAIMessageVO.getGameStatus() == 2) {  // 用户平局
                userScore += UserGameSuccessConstant.AI_GAME_DEAD_HEAT;
                gameDeadHeatCounts += 1;
            }
            gameTotalCounts += 1;
            gameAiCounts += 1;
            // 将分数传入工具方法得到用户等级
            String userLevel = UserLevelUtil.getUserLevelByUserScore(userScore);

            // 组装数据
            User user1 = User.builder().
                    id(userId).userScore(userScore)
                    .userLevel(userLevel)
                    .gameTotalCounts(gameTotalCounts)
                    .gameAiCounts(gameAiCounts)
                    .gameSuccessCounts(gameSuccessCounts)
                    .gameFailCounts(gameFailCounts)
                    .gameDeadHeatCounts(gameDeadHeatCounts)
                    .build();
            userMapper.update(user1);   // 更新
        }

        if (gameAIMessageVO.getGameStatus() == 1 || gameAIMessageVO.getGameStatus() == 3) {
            // AI走了一步，其他情况AI没走，是工具方法判断的，AI没有计算
            // 把AI的选择也存到数据库
            GameDetails gameAIDetails = GameDetails.builder()
                    .gameId(gameAIMessageDTO.getGameId())
                    .moveX(gameAIMessageVO.getAIPieces().getX())
                    .moveY(gameAIMessageVO.getAIPieces().getY())
                    .stepOrder(gameAIMessageDTO.getStepOrder())
                    .color((byte) 1)
                    .build();
            gameDetailsMapper.insert(gameAIDetails);
        }


        return gameAIMessageVO;
    }
}
