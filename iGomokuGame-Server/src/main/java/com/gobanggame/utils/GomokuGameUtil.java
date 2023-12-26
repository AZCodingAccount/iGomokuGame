package com.gobanggame.utils;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-14 15:04
 * @description: 判断当前棋局状态的选项
 **/
public class GomokuGameUtil {

    /**
     * @param chessBoard
     * @return int
     * @author AlbertZhang
     * @description 判断游戏是否结束
     * @date 2023-12-25 18:44
     **/
    public static int isGameOver(Integer[][] chessBoard) {
        if (chessBoard == null) {
            return 0;
        }
        boolean isDeadHeat = true;

        for (int x = 0; x < chessBoard.length; x++) {
            for (int y = 0; y < chessBoard[x].length; y++) {
                Integer player = chessBoard[x][y];
                if (player == null || player == 0) {
                    isDeadHeat = false;
                }
                if (player != 0 && (checkDirection(chessBoard, x, y, 1, 0, player) ||
                        checkDirection(chessBoard, x, y, 0, 1, player) ||
                        checkDirection(chessBoard, x, y, 1, 1, player) ||
                        checkDirection(chessBoard, x, y, 1, -1, player))) {
                    return player;  // 返回获胜方
                }
            }
        }
        if (isDeadHeat) {
            return 3;   // 平局
        }
        return 0;  // 游戏尚未结束
    }

    /**
     * @param chessBoard 当前棋盘
     * @param x          要评估的点的x坐标
     * @param y          要评估的点的y坐标
     * @param deltaX     标识每次评估x的偏移量
     * @param deltaY     标识每次评估x的偏移量
     * @param player     当前评估的坐标的棋子的颜色
     * @return boolean
     * @author AlbertZhang
     * @description 检查每个方向
     * @date 2023-12-25 18:45
     **/
    private static boolean checkDirection(Integer[][] chessBoard, int x, int y, int deltaX, int deltaY, Integer player) {
        int count = 1;

        for (int i = 1; i < 5; i++) {
            int newX = x + i * deltaX;
            int newY = y + i * deltaY;
            if (!isValidPoint(chessBoard, newX, newY) || chessBoard[newX][newY] != player) {
                break;
            }
            count++;
        }

        for (int i = 1; i < 5; i++) {
            int newX = x - i * deltaX;
            int newY = y - i * deltaY;
            if (!isValidPoint(chessBoard, newX, newY) || chessBoard[newX][newY] != player) {
                break;
            }
            count++;
        }

        return count >= 5;
    }

    /*
     * 越没越界
     * */
    private static boolean isValidPoint(Integer[][] chessBoard, int x, int y) {
        return x >= 0 && x < chessBoard.length && y >= 0 && y < chessBoard[0].length;
    }

}


