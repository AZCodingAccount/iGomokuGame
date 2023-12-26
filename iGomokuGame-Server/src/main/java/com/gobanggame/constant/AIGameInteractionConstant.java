package com.gobanggame.constant;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-12 15:10
 * @description: AI互动类—定义前端下棋时的一些AI互动消息
 **/
public class AIGameInteractionConstant {

    // 游戏结束
    public static final String GAME_SUCCESS_AI = "AI胜利了";
    public static final String GAME_SUCCESS_PERSON = "恭喜你胜利了";
    public static final String GAME_DEAD_HEAT = "哎呀，再战一场，再战一场";

    // 对AI有利的局势
    public static final String OPEN_FOUR_AI = "我活四了，投降吧哈哈！！";
    public static final String OPEN_FOUR_AI_Twice = "我有不止一个活四，你凉的透透的了";
    public static final String OPEN_THREE_AI = "我活三了唉";
    public static final String OPEN_THREE_AI_Twice = "我有不止一个活三，GameOver啦";

    // 对用户有利的局势
    public static final String OPEN_FOUR_PLAYER_TWICE = "玩家双活四";
    public static final String OPEN_FOUR_PLAYER = "你居然活四了??!不可能，绝对不可能";
    public static final String OPEN_THREE_PLAYER = "玩家活三";
    public static final String OPEN_THREE_PLAYER_Twice = "玩家双活三";

    // 局势提示信息
    public static final String BOARD_STATUS_UNCERTAIN = "局势焦灼";


}
