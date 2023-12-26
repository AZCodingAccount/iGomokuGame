package com.gobanggame.utils;

import com.gobanggame.constant.AIGameInteractionConstant;
import com.gobanggame.pojo.pojo.Coordinate;
import com.gobanggame.pojo.vo.GameAIMessageVO;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumMap;
import java.util.Map;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 16:05
 * @description: 棋盘计算类——AI实际工作的部分
 **/
@Slf4j
public class CalChessBoard {
    // 棋子状态常量
    private static final byte EMPTY = 0;
    public static final byte BLACK = 1;
    public static final byte WHITE = 2;
    // 当前所属玩家的回合，深度搜索时使用，这个跟棋盘状态对上，在模拟落子的时候就不用判断了。AI默认是白子
    private static final byte PERSON_PLAYER = 1;
    private static final byte AI_PLAYER = 2;
    private final Byte[][] chessBoard;    // 用来标识当前棋盘的状态
    private final int searchDepth;     // 表示搜索深度

    private int searchNodeCount = 0;    // 记录当前搜索的节点个数

    private int cutBranchCount = 0;     // 记录当前的剪枝个数


    private static final Map<ChessBoardType, Integer> SCORE_MAP = new EnumMap<>(ChessBoardType.class);

    // 静态代码块初始化棋型评分数组
    // 注意，这个数组就是经验法则得出的，要注意的是，如果需要下面程序正常运行，五子连珠的得分一定要高出其他的几个数量级
    // 这是由于我在进行评估的时候进行了剪枝，如果五子连珠小一点，恰好又这个得分太高或者太低，就会错误的剪枝。
    // 一般来说，几十倍就差不多了。就算有两个活四，才是8倍。
    static {
        SCORE_MAP.put(ChessBoardType.FIVE_IN_A_ROW, 5000000);
        SCORE_MAP.put(ChessBoardType.LIVE_FOUR, 100000);
        SCORE_MAP.put(ChessBoardType.DEAD_FOUR, 10000);
        SCORE_MAP.put(ChessBoardType.LIVE_THREE, 8000);
        SCORE_MAP.put(ChessBoardType.DEAD_THREE, 500);
        SCORE_MAP.put(ChessBoardType.LIVE_TWO, 50); // 尽可能小，以免影响判断
        SCORE_MAP.put(ChessBoardType.DEAD_TWO, 10);
        SCORE_MAP.put(ChessBoardType.SINGLE, 5);
    }

    // 构造方法用于接收工具类传入的参数
    public CalChessBoard(Byte[][] chessBoard, int depth) {
        this.chessBoard = chessBoard;
        this.searchDepth = depth;
    }

    // 判断棋盘某个位置是不是为空——>用于搜索时使用
    public boolean isEmpty(byte x, byte y) {
        return chessBoard[x][y] == EMPTY;
    }

    /*先在棋盘上放置棋子，用于初步评估函数和深度搜索时模仿棋子放置时使用。这里我没用，直接给棋盘位置赋值了。忘记了......*/
    public void makeMove(byte x, byte y, byte color) {
        chessBoard[x][y] = color;
    }

    /*在棋盘中移除棋子，用于评估函数和深度搜索时移除放置后的棋子使用*/
    public void removeMove(byte x, byte y) {
        chessBoard[x][y] = EMPTY;
    }

    // 主要的工作方法
    public GameAIMessageVO work() {
        // 1代表玩家，2代表AI
        return work(chessBoard, (byte) 2, searchDepth);
    }

    public GameAIMessageVO work(Byte[][] board, byte player, int depth) {
        GameAIMessageVO gameAIResult = new GameAIMessageVO();
        Coordinate bestMove;
        // 根据不同的搜索深度调用不同的方法
        if (depth > 1) {
            bestMove = findBestMove(board, player, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else if (depth == 1) {
            bestMove = findBestMoveNoSearch(board, player);
        } else {    // 最不聪明的那个，最简单的难度,depth=0
            bestMove = findBestMoveOddDepth(board, player, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        // 开始拼装返回结果
        GameAIMessageVO.Coordinate coordinate = new GameAIMessageVO.Coordinate();
        coordinate.setX((byte) bestMove.getX());
        coordinate.setY((byte) bestMove.getY());
        gameAIResult.setAIPieces(coordinate);  // 设置AI的最佳落子点

        // 下面是设置游戏状态和互动消息
        chessBoard[bestMove.getX()][bestMove.getY()] = WHITE;   // AI把这个棋先下了
        int[] gameStatus = gameStatus(chessBoard, bestMove, AI_PLAYER); // 获取现在游戏的状态
        if (isGameOver(bestMove) == AI_PLAYER) {
            // AI赢了
            gameAIResult.setGameStatus(1);
            gameAIResult.setMessage(AIGameInteractionConstant.GAME_SUCCESS_AI);
        } else if (gameStatus[0] != 0) {
            gameAIResult.setGameStatus(3);
            if (gameStatus[0] > 1) {
                // AI双活四
                gameAIResult.setMessage(AIGameInteractionConstant.OPEN_FOUR_AI_Twice);
            } else {
                // AI活四
                gameAIResult.setMessage(AIGameInteractionConstant.OPEN_FOUR_AI);
            }
        } else if (gameStatus[1] != 0) {
            gameAIResult.setGameStatus(3);
            if (gameStatus[1] > 1) {
                // AI双活三
                gameAIResult.setMessage(AIGameInteractionConstant.OPEN_THREE_AI_Twice);
            } else {
                // AI活三
                gameAIResult.setMessage(AIGameInteractionConstant.OPEN_THREE_AI);
            }
        } else {
            // 棋局进行中，且没有消息传输
            gameAIResult.setGameStatus(3);
            gameAIResult.setMessage("");
        }
        log.info("本次搜索深度为{}，共搜索{}个节点，剪去{}个分支", depth, searchNodeCount, cutBranchCount);
        log.info("本次数据传输结果为{}", gameAIResult);
        return gameAIResult;
    }

    // alpha-beta剪枝和极大极小搜索的核心算法
    // 初始化alpha为最大下界，beta为最小上界。假设两者都是非常聪明的。
    //              1                   max     depth=0
    //   2      3       4        5      min     depth=1
    // 6  7   8  9   10  11   12  13    max     depth=2
    // https://zhuanlan.zhihu.com/p/65219446 参考的文章

    private Coordinate findBestMove(Byte[][] board, byte player, int depth, int alpha, int beta) {
        Coordinate bestMove = new Coordinate(-1, -1);   // 初始化当前的最佳移动坐标
        int totalScore = 0;
        Coordinate coordinate;
        // 这个主要是为了判断模拟对于空子的争夺
        // 如果搜索深度是奇数，说明最后一步轮到玩家（对手）走，这个时候评估AI（我的）的得分
        if (searchDepth % 2 != 0) {
            coordinate = evaluateBoard(board, (byte) (3 - player));
            totalScore = coordinate.getScore();
            if (player == AI_PLAYER) {  // 如果本轮是AI，但是你评估的是对面的得分，你就得翻转一下
                totalScore = -totalScore;
            }
        } else {    // 搜索深度为偶数，说明最后一步轮到AI走，这个时候评估玩家(对手)的得分
            coordinate = evaluateBoard(board, player);
            totalScore = coordinate.getScore();
            if (player == PERSON_PLAYER) {  // 如果本轮是玩家，但是你评估的是AI的得分，你就得翻转一下
                totalScore = -totalScore;
            }
        }
        if (depth == 0) {   //  搜索到最深处了，返回
            return new Coordinate(totalScore, -1, -1);
        }
        /*
         * 可以提前结束。但是还需要考虑一个点，就是当前剪枝的时候用户已经4子了。这个时候评估函数已经模拟出我们接下来要下哪边。但是坐标没带过来
         * 五子连珠剪枝的时候需要把模仿出五子连珠的这个坐标记录一下返回，这个函数我已经改写完了，就是那个评估棋盘的函数
         * 需要注意的是，就是因为有这个根据五子连珠剪枝的函数，所以每轮都需要评估一下当前局势，这样每轮除了alpha-beta剪枝还多出了一个剪枝的手段
         * */
        // if (totalScore > SCORE_MAP.get(ChessBoardType.FIVE_IN_A_ROW) || totalScore < -SCORE_MAP.get(ChessBoardType.FIVE_IN_A_ROW)) {
        //     log.info("剪枝了，{}走到{}，{}五子连珠了", player, coordinate.getX(), coordinate.getY());
        //     return new Coordinate(totalScore, coordinate.getX(), coordinate.getY());
        // }
        // 初始化最大分数，我们希望ai的分数尽可能大，玩家的分数尽可能小。因此这样初始化
        int bestScore = (player == AI_PLAYER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // 开始循环评估棋盘中其他的空位
        outerLoop:
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // 评估每个空位
                if (board[i][j] == EMPTY) {

                    board[i][j] = player;   // 模拟当前玩家落子
                    // 关键极大极小搜索函数
                    Coordinate playerMove = findBestMove(board, (byte) (3 - player), depth - 1, alpha, beta);
                    int currentScore = playerMove.getScore();
                    board[i][j] = EMPTY; // 撤销落子
                    searchNodeCount += 1;   // 类似于日志，记录一下搜索的个数

                    if (player == AI_PLAYER && currentScore > bestScore) {  // 极大层
                        // 如果当前player是AI，那么看当前棋盘分数大于最好分数，得到了就更新一下bestMove
                        bestScore = currentScore;
                        bestMove = new Coordinate(bestScore, i, j);
                        alpha = Math.max(alpha, bestScore); // 更新一下alpha的值
                    } else if (player != AI_PLAYER && currentScore < bestScore) {   // 极小层
                        // 如果当前player是玩家，那么看当前棋盘分数是否小于最好分数，得到了就更新一下bestMove
                        bestScore = currentScore;
                        bestMove = new Coordinate(bestScore, i, j);
                        beta = Math.min(beta, bestScore);  // 更新一下beta的值
                    }
                    // System.out.println(i + ":" + j + "的分数为" + currentScore);
                    if (beta <= alpha) {
                        cutBranchCount += 1;
                        break outerLoop; // Alpha-Beta剪枝
                    }
                }
            }
        }
        // 判断当前是不是更新了，然后把当前这个选择找到的最优分数和坐标进行返回
        return (bestScore == Integer.MIN_VALUE || bestScore == Integer.MAX_VALUE) ?
                bestMove : new Coordinate(bestScore, bestMove.getX(), bestMove.getY());
    }


    // 历史的眼泪，当时为了省事写了一个偶数和奇数，现在统一到bestMove里面了
    private Coordinate findBestMoveOddDepth(Byte[][] board, byte player, int depth, int alpha, int beta) {
        Coordinate bestMove = new Coordinate(-1, -1);   // 初始化当前的最佳移动坐标
        Coordinate coordinate = evaluateBoard(board, (byte) (3 - player));
        int totalScore = coordinate.getScore();
        if (player == AI_PLAYER) {
            totalScore = -totalScore;
        }
        // 可以提前结束。但是还需要考虑一个点，就是当前剪枝的时候用户已经4子了。这个时候评估函数已经模拟出我们接下来要下哪边。但是坐标没带过来
        // 因此改写一下函数返回5子连珠的坐标
        if (depth == 0) {
            return new Coordinate(totalScore, -1, -1);
        }
        // if (totalScore > SCORE_MAP.get(ChessBoardType.FIVE_IN_A_ROW) || totalScore < -SCORE_MAP.get(ChessBoardType.FIVE_IN_A_ROW)) {
        //     // log.info("剪枝了，{}走到{}，{}五子连珠了", player, coordinate.getX(), coordinate.getY());
        //     return new Coordinate(totalScore, coordinate.getX(), coordinate.getY());
        // }
        // 初始化最大分数，我们希望ai的分数尽可能大，玩家的分数尽可能小。因此这样初始化
        int bestScore = (player == AI_PLAYER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // 开始循环评估棋盘中其他的空位
        outerLoop:
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // 评估每个空位
                if (board[i][j] == EMPTY) {

                    board[i][j] = player;   // 模拟当前玩家落子
                    Coordinate playerMove = findBestMoveOddDepth(board, (byte) (3 - player), depth - 1, alpha, beta);
                    int currentScore = playerMove.getScore();

                    board[i][j] = EMPTY; // 撤销落子
                    searchNodeCount += 1;

                    if (player == AI_PLAYER && currentScore > bestScore) {
                        // 如果当前player是AI，那么看当前棋盘分数大于最好分数，得到了就更新一下bestMove
                        bestScore = currentScore;
                        bestMove = new Coordinate(bestScore, i, j);
                        alpha = Math.max(alpha, bestScore); // 更新一下alpha的值
                    } else if (player != AI_PLAYER && currentScore < bestScore) {
                        // 如果当前player是玩家，那么看当前棋盘分数是否小于最好分数，得到了就更新一下bestMove
                        bestScore = currentScore;
                        bestMove = new Coordinate(bestScore, i, j);
                        beta = Math.min(beta, bestScore);  // 更新一下beta的值
                    }
                    // System.out.println(i + ":" + j + "的分数为" + currentScore);
                    if (beta <= alpha) {
                        cutBranchCount += 1;
                        break outerLoop; // Alpha-Beta剪枝
                    }
                }
            }
        }
        // 判断当前是不是更新了，然后把当前这个选择找到的最优分数和坐标进行返回
        return (bestScore == Integer.MIN_VALUE || bestScore == Integer.MAX_VALUE) ?
                bestMove : new Coordinate(bestScore, bestMove.getX(), bestMove.getY());
    }


    // 历史的眼泪，当时为了省事写了一个偶数和奇数，现在统一到bestMove里面了
    private Coordinate findBestMoveEvenDepth(Byte[][] board, byte player, int depth, int alpha, int beta) {
        Coordinate bestMove = new Coordinate(-1, -1);   // 初始化当前的最佳移动坐标
        Coordinate coordinate = evaluateBoard(board, player);
        int totalScore = coordinate.getScore();
        if (player != AI_PLAYER) {
            totalScore = -totalScore;
        }
        // 可以提前结束。但是还需要考虑一个点，就是当前剪枝的时候用户已经4子了。这个时候评估函数已经模拟出我们接下来要下哪边。但是坐标没带过来
        // 因此改写一下函数返回5子连珠的坐标
        if (depth == 0) {
            return new Coordinate(totalScore, -1, -1);
        }
        if (totalScore > SCORE_MAP.get(ChessBoardType.FIVE_IN_A_ROW) || totalScore < -SCORE_MAP.get(ChessBoardType.FIVE_IN_A_ROW)) {
            log.info("剪枝了，{}走到{}，{}五子连珠了", player, coordinate.getX(), coordinate.getY());
            return new Coordinate(totalScore, coordinate.getX(), coordinate.getY());
        }
        // 初始化最大分数，我们希望ai的分数尽可能大，玩家的分数尽可能小。因此这样初始化
        int bestScore = (player == AI_PLAYER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // 开始循环评估棋盘中其他的空位
        outerLoop:
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // 评估每个空位
                if (board[i][j] == EMPTY) {

                    board[i][j] = player;   // 模拟当前玩家落子
                    Coordinate playerMove = findBestMoveEvenDepth(board, (byte) (3 - player), depth - 1, alpha, beta);
                    int currentScore = playerMove.getScore();

                    board[i][j] = EMPTY; // 撤销落子
                    searchNodeCount += 1;

                    if (player == AI_PLAYER && currentScore > bestScore) {
                        // 如果当前player是AI，那么看当前棋盘分数大于最好分数，得到了就更新一下bestMove
                        bestScore = currentScore;
                        bestMove = new Coordinate(bestScore, i, j);
                        alpha = Math.max(alpha, bestScore); // 更新一下alpha的值
                    } else if (player != AI_PLAYER && currentScore < bestScore) {
                        // 如果当前player是玩家，那么看当前棋盘分数是否小于最好分数，得到了就更新一下bestMove
                        bestScore = currentScore;
                        bestMove = new Coordinate(bestScore, i, j);
                        beta = Math.min(beta, bestScore);  // 更新一下beta的值
                    }
                    // System.out.println(i + ":" + j + "的分数为" + currentScore);
                    if (beta <= alpha) {
                        cutBranchCount += 1;
                        break outerLoop; // Alpha-Beta剪枝
                    }
                }
            }
        }
        // 判断当前是不是更新了，然后把当前这个选择找到的最优分数和坐标进行返回
        return (bestScore == Integer.MIN_VALUE || bestScore == Integer.MAX_VALUE) ?
                bestMove : new Coordinate(bestScore, bestMove.getX(), bestMove.getY());
    }

    // 这个用于不需要进行深度搜索的时候使用，专门为简单的难度又写了一个方法
    private static Coordinate findBestMoveNoSearch(Byte[][] board, byte player) {
        Coordinate bestMove = new Coordinate(-1, -1);
        int bestScore = (player == AI_PLAYER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) { // 空位
                    // 我方落到这里的收益值
                    board[i][j] = player; // 尝试落子
                    int AIScore = evaluateBoard(board, player).getScore(); // 评估棋盘
                    board[i][j] = 0; // 撤销落子

                    // 敌方落到这里的收益值
                    board[i][j] = (byte) (3 - player); // 尝试落子
                    int PersonScore = evaluateBoard(board, player).getScore(); // 评估棋盘
                    board[i][j] = 0; // 撤销落子
                    int score = (int) (1.5 * AIScore - PersonScore);

                    if ((player == AI_PLAYER && score > bestScore) ||
                            (player != AI_PLAYER && score < bestScore)) {
                        bestScore = score;
                        bestMove = new Coordinate(bestScore, i, j);
                    }
                }
            }
        }
        return bestMove;
    }


    /*
     * 评估棋盘的方法
     * */
    private static Coordinate evaluateBoard(Byte[][] board, byte player) {
        /*
        在这个方法需要从三个维度评估
                1：评估当前棋局对我方的有力局势
                2：评估棋局对敌方的有利局势
                3：评估空位的潜在威胁（如果使用10111这种五元组、四元组的话就不需要，但是不优雅）
        */
        int totalScore = 0;
        int x = 0, y = 0;   // 记录选择的五子连珠中落子的坐标
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != EMPTY) {
                    if (board[i][j] == player) {    // 当前评估的棋子是我放到额
                        int score = evaluatePosition(board, i, j, player);
                        if (score > SCORE_MAP.get(ChessBoardType.FIVE_IN_A_ROW)) {
                            x = i;
                            y = j;
                        }
                        totalScore += score; // 计算棋局总分
                    } else {    // 如果当前评估的棋子是敌方的
                        int score = evaluatePosition(board, i, j, (byte) (3 - player)); // 计算棋局总分
                        if (score > SCORE_MAP.get(ChessBoardType.FIVE_IN_A_ROW)) {
                            x = i;
                            y = j;
                        }
                        totalScore -= score;
                    }
                } else {
                    // 2为白,1为黑
                    board[i][j] = (byte) (3 - player);  // 模拟对手在空位落子
                    int opponentImpact = evaluatePosition(board, i, j, board[i][j]);
                    // 如果五子连珠和，记录一下x和y
                    // if (opponentImpact > SCORE_MAP.get(ChessBoardType.FIVE_IN_A_ROW)) {
                    //     x = i;
                    //     y = j;
                    //     totalScore = (int) (totalScore - (0.2 * opponentImpact));
                    // } else {
                 /*
                *  如果对手落子会对我不利，则减少评分，这个比例太大的话，如果都是一比一的话对于冲四的局势AI就会扭扭捏捏
                           且会考虑不到对手五个的情况（在剪枝的时候）比如黑子眠四，白子（AI）活三，他如果模拟的话就会把黑子这个眠四忽略掉
                     最大值-2*最大值=-最大值。
                  也不能太小，太小的话，没有办法判断对方对我们的潜在威胁
                        考虑这样一个情况，如果你下到这里白子能活三，但是对面下到另一个地方两个活三。如果太小0.2AI给出的选择是让自己活三
                    而不去阻挡别人双活三。
                        针对这个问题最好的解决方法当然是不剪枝，然后系数大于0.5即可。但是这里还是做一下判断，针对不同的情况做不同处理
                * */
                    totalScore = (int) (totalScore - opponentImpact);

                    // }
                    board[i][j] = EMPTY;    // 恢复空位


                }
            }
        }
        return new Coordinate(totalScore, x, y);
    }

    /**
     * @param board  棋盘状态
     * @param x      要评估的点的x坐标
     * @param y      要评估的点的y坐标
     * @param player 要评估的点的棋子颜色（黑白）
     * @return int
     * @author AlbertZhang
     * @description 根据棋局局势返回一个点的所有得分
     * @date 2023-12-25 17:07
     **/
    private static int evaluatePosition(Byte[][] board, int x, int y, byte player) {
        int positionScore = 0;
        // 检查水平、垂直、两个对角线方向
        positionScore += checkLine(board, x, y, 1, 0, player); // 水平
        positionScore += checkLine(board, x, y, 0, 1, player); // 垂直
        positionScore += checkLine(board, x, y, 1, 1, player); // 对角线
        positionScore += checkLine(board, x, y, 1, -1, player); // 反对角线
        return positionScore;
    }

    /**
     * @param board  棋盘
     * @param x      要评估的点的x坐标
     * @param y      要评估的点的y坐标
     * @param deltaX 标识每次评估x的偏移量
     * @param deltaY 标识每次评估x的偏移量
     * @param player 当前评估的坐标的棋子的颜色
     * @return int  棋局的得分
     * @author AlbertZhang
     * @description 根据棋盘局势返回当前点一个方向的得分
     * @date 2023-12-25 17:04
     **/
    private static int checkLine(Byte[][] board, int x, int y, int deltaX, int deltaY, byte player) {
        int count = 1;  // 记录连在一起的棋子数目
        int block = 0;  // 记录一端被阻挡，两端都被阻挡
        int score = 0;  // 最终这个位置返回的分数

        // 向一个方向延伸
        for (int i = 1; i < 5; i++) {
            int newX = x + i * deltaX;
            int newY = y + i * deltaY;
            // 防止越界
            if (newX >= 0 && newY >= 0 && newX < board.length && newY < board[0].length) {
                if (board[newX][newY] == player) {  // 如果当前这个非空的棋子是我方的，计入累积的数量
                    count++;
                } else {    // 如果当前这个棋子是敌方的，标记这边卡住了
                    if (board[newX][newY] != EMPTY) {
                        block++;
                    }
                    break;  // 或者棋子为空直接退出循环去另一个方向
                }
            }
        }

        // 向相反方向延伸
        for (int i = 1; i < 5; i++) {
            int newX = x - i * deltaX;
            int newY = y - i * deltaY;
            // 这个同理
            if (newX >= 0 && newY >= 0 && newX < board.length && newY < board[0].length) {
                if (board[newX][newY] == player) {
                    count++;
                } else {
                    if (board[newX][newY] != EMPTY) {
                        block++;
                    }
                    break;
                }
            }
        }

        // 根据连子数量和阻挡情况来确定棋型
        // 综合考虑了三种情况
        score = switch (count) {
            case 4 -> block == 0 ? SCORE_MAP.get(ChessBoardType.LIVE_FOUR) :
                    (block == 1 ? SCORE_MAP.get(ChessBoardType.DEAD_FOUR) : 0); // 活四、死四或无效
            case 3 -> block == 0 ? SCORE_MAP.get(ChessBoardType.LIVE_THREE) :
                    (block == 1 ? SCORE_MAP.get(ChessBoardType.DEAD_THREE) : 0); // 活三、死三或无效
            case 2 -> block == 0 ? SCORE_MAP.get(ChessBoardType.LIVE_TWO) :
                    (block == 1 ? SCORE_MAP.get(ChessBoardType.DEAD_TWO) : 0); // 活二、死二或无效
            case 1 -> SCORE_MAP.get(ChessBoardType.SINGLE); // 单1
            case 5, 6, 7, 8, 9 -> SCORE_MAP.get(ChessBoardType.FIVE_IN_A_ROW); // 五子连珠或超过五子，均直接获胜
            default -> 0;
        };

        return score;
    }


    /*
     * 判断游戏是否结束
     * */
    public byte isGameOver(Coordinate lastMove) {
        if (lastMove == null) {
            return EMPTY;
        }
        int x = lastMove.getX();
        int y = lastMove.getY();
        byte currentPlayer = chessBoard[x][y];  // 获取当前用户是黑色方还是白色方

        // 检查四个方向，有一个有五子连珠就返回这个获胜
        if (checkDirection(x, y, 1, 0, currentPlayer) || // 水平
                checkDirection(x, y, 0, 1, currentPlayer) || // 垂直
                checkDirection(x, y, 1, 1, currentPlayer) || // 对角线
                checkDirection(x, y, 1, -1, currentPlayer)) { // 反对角线
            return currentPlayer;
        }
        return EMPTY;   // 游戏还未结束
    }


    /*
     * 判断每个方向有没有五子连珠，有的话就获胜
     * */
    private boolean checkDirection(int x, int y, int deltaX, int deltaY, byte player) {
        int count = 1;

        // 检查一个方向
        for (int i = 1; i < 5; i++) {
            int newX = x + i * deltaX;
            int newY = y + i * deltaY;
            if (isValidPoint(newX, newY) || chessBoard[newX][newY] != player) {
                break;
            }
            count++;
        }

        // 检查相反方向
        for (int i = 1; i < 5; i++) {
            int newX = x - i * deltaX;
            int newY = y - i * deltaY;
            if (isValidPoint(newX, newY) || chessBoard[newX][newY] != player) {
                break;
            }
            count++;
        }

        return count >= 5;
    }

    // 辅助方法：检查点是否在棋盘内
    private boolean isValidPoint(int x, int y) {
        return x < 0 || x >= chessBoard.length || y < 0 || y >= chessBoard[0].length;
    }


    /*
     * 判断移动后棋盘的状态   // 1 活四   2活三  todo:后续扩展 死四，死三，1011这种棋型
     * */
    public static int[] gameStatus(Byte[][] board, Coordinate lastMove, byte player) {
        int x = lastMove.getX();
        int y = lastMove.getY();

        int positionScore = 0;
        int countLive4 = 0;
        int countLive3 = 0;
        // 检查水平、垂直、两个对角线方向
        // 后续可以根据这个判断眠四，眠三。
        positionScore = checkLine(board, x, y, 1, 0, player); // 水平
        if (positionScore == SCORE_MAP.get(ChessBoardType.LIVE_FOUR)) {
            countLive4++;
        } else if (positionScore == SCORE_MAP.get(ChessBoardType.LIVE_THREE)) {
            countLive3++;
        }
        positionScore = checkLine(board, x, y, 0, 1, player); // 垂直
        if (positionScore == SCORE_MAP.get(ChessBoardType.LIVE_FOUR)) {
            countLive4++;
        } else if (positionScore == SCORE_MAP.get(ChessBoardType.LIVE_THREE)) {
            countLive3++;
        }
        positionScore = checkLine(board, x, y, 1, 1, player); // 对角线
        if (positionScore == SCORE_MAP.get(ChessBoardType.LIVE_FOUR)) {
            countLive4++;
        } else if (positionScore == SCORE_MAP.get(ChessBoardType.LIVE_THREE)) {
            countLive3++;
        }
        positionScore = checkLine(board, x, y, 1, -1, player); // 反对角线
        if (positionScore == SCORE_MAP.get(ChessBoardType.LIVE_FOUR)) {
            countLive4++;
        } else if (positionScore == SCORE_MAP.get(ChessBoardType.LIVE_THREE)) {
            countLive3++;
        }
        int[] arr = new int[2];
        arr[0] = countLive4;
        arr[1] = countLive3;
        return arr;
    }
}
