package com.gobanggame.utils;

import com.gobanggame.constant.UserLevelConstant;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-13 21:44
 * @description: 用于操作用户的等级，根据不同的用户分数把用户映射到不同的等级上
 **/
public class UserLevelUtil {

    // 这个工具方法用于转换userScore—>正确的userLevel里面
    public static String getUserLevelByUserScore(Integer userScore) {
        if (userScore < UserLevelConstant.userLevelScoreMap.get(UserLevelConstant.LEVEL1)) {
            return UserLevelConstant.LEVEL1;
        } else if (userScore < UserLevelConstant.userLevelScoreMap.get(UserLevelConstant.LEVEL2)) {
            return UserLevelConstant.LEVEL2;
        } else if (userScore < UserLevelConstant.userLevelScoreMap.get(UserLevelConstant.LEVEL3)) {
            return UserLevelConstant.LEVEL3;
        } else if (userScore < UserLevelConstant.userLevelScoreMap.get(UserLevelConstant.LEVEL4)) {
            return UserLevelConstant.LEVEL4;
        } else if (userScore < UserLevelConstant.userLevelScoreMap.get(UserLevelConstant.LEVEL5)) {
            return UserLevelConstant.LEVEL5;
        } else {
            return UserLevelConstant.LEVEL6;
        }
    }

}
