package com.gobanggame;

import com.gobanggame.utils.GomokuGameUtil;
import org.junit.jupiter.api.Test;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-19 21:43
 * @description:
 **/
public class BoardTest {
    @Test
    public void test() {
        Integer[][] board = new Integer[15][15];
        for (int i = 0; i < board.length; i++) {
            for (int i1 = 0; i1 < board.length; i1++) {
                board[i][i1] = 0;
            }
        }
        for (int i = 1; i < 6; i++) {
            board[i][0] = 1;
            board[i][1] = 2;
        }
        board[1][1] = 0;
        int game = GomokuGameUtil.isGameOver(board);
        System.out.println(game);

    }
}
