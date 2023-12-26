package com.gobanggame.constant;

import java.util.HashMap;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-13 21:45
 * @description: 用户等级常量类
 **/
public class UserLevelConstant {
    public static HashMap<String, Integer> userLevelScoreMap = new HashMap<>();


    public static final String LEVEL1 = "初入江湖";
    public static final String LEVEL2 = "略知一二";
    public static final String LEVEL3 = "小有所成";
    public static final String LEVEL4 = "登峰造极";
    public static final String LEVEL5 = "胜天半子";
    public static final String LEVEL6 = "渡劫飞升";

    // 每个等级对应的分数
    static {
        userLevelScoreMap.put(LEVEL1,100);
        userLevelScoreMap.put(LEVEL2,500);
        userLevelScoreMap.put(LEVEL3,1000);
        userLevelScoreMap.put(LEVEL4,5000);
        userLevelScoreMap.put(LEVEL5,10000);
    }
}
