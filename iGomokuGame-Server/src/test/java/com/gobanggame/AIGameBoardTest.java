// package com.gobanggame;
//
// import com.gobanggame.pojo.vo.GameAIMessageVO;
// import com.gobanggame.utils.CalChessBoard;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
//
// import static org.springframework.test.util.AssertionErrors.assertEquals;
// import static org.springframework.test.util.AssertionErrors.assertNotNull;
//
// /**
//  * @program: gobanggame
//  * @author: AlbertZhang
//  * @create: 2023-12-12 20:22
//  * @description: 测试AI下棋
//  **/
// public class AIGameBoardTest {
//
//     private final int depth=4;
//
//     // 普通测试，测试当前
//     @Test
//     @DisplayName("普通测试")
//     public void testFindBestMoveForLiveThree() {
//
//
//         Byte[][] board = new Byte[15][15];
//         initializeBoard(board);
//
//         CalChessBoard chessBoard = new CalChessBoard(board, depth);
//
//         // 设置棋盘状态
//         chessBoard.makeMove((byte) 6, (byte) 7, CalChessBoard.WHITE); // 白子
//         chessBoard.makeMove((byte) 8, (byte) 7, CalChessBoard.WHITE); // 白子
//
//         // 假设黑子也有一些棋，但不在关键位置
//         chessBoard.makeMove((byte) 3, (byte) 3, CalChessBoard.BLACK);
//         chessBoard.makeMove((byte) 4, (byte) 4, CalChessBoard.BLACK);
//
//         GameAIMessageVO result = chessBoard.work();
//
//         // 验证AI找到的最佳移动是否是 (7,7)，形成活三
//         // assertNotNull("应该找到最佳移动", result.getAIPieces());
//         System.out.println(result);
//         // assertEquals("最佳移动应该是形成活三的位置", 7, result.getAIPieces().getX());
//         // assertEquals("最佳移动应该是形成活三的位置", 7, result.getAIPieces().getY());
//     }
//
//
//
//     @Test
//     @DisplayName("测试AI是否能够正确的防守活四")
//     public void testAIAttackAndDefense() {
//         Byte[][] board = new Byte[15][15];
//         initializeBoard(board);
//         CalChessBoard chessBoard = new CalChessBoard(board, depth);
//
//         // 设置棋盘初始状态，包括AI和对手的几步走棋
//         // AI（白子）的走棋
//         chessBoard.makeMove((byte) 5, (byte) 5, CalChessBoard.WHITE);
//         chessBoard.makeMove((byte) 5, (byte) 6, CalChessBoard.WHITE);
//
//         // 对手（黑子）的走棋，即将形成四子连线
//         chessBoard.makeMove((byte) 4, (byte) 4, CalChessBoard.BLACK);
//         chessBoard.makeMove((byte) 6, (byte) 4, CalChessBoard.BLACK);
//         chessBoard.makeMove((byte) 7, (byte) 4, CalChessBoard.BLACK);
//
//         // 期望的走棋
//         byte expectedX = 5;
//         byte expectedY = 4;
//         // AI进行下一步走棋
//         GameAIMessageVO result = chessBoard.work();
//         System.out.println(result);
//         // 检查AI是否正确执行防守
//         assertCorrectMove(result.getAIPieces(), expectedX, expectedY);
//     }
//
//     @Test
//     @DisplayName("测试AI是否能正确进攻活四")
//     public void testAIHandleLiveFour() {
//         Byte[][] board = new Byte[15][15];
//         initializeBoard(board);
//         CalChessBoard chessBoard = new CalChessBoard(board, depth);
//
//         // 设置AI（白子）即将形成活四的局面
//         chessBoard.makeMove((byte) 3, (byte) 3, CalChessBoard.WHITE);
//         chessBoard.makeMove((byte) 3, (byte) 4, CalChessBoard.WHITE);
//         chessBoard.makeMove((byte) 3, (byte) 5, CalChessBoard.WHITE);
//
//         // 对手（黑子）的走棋，形成一定威胁
//         chessBoard.makeMove((byte) 7, (byte) 7, CalChessBoard.BLACK);
//         chessBoard.makeMove((byte) 8, (byte) 8, CalChessBoard.BLACK);
//         chessBoard.makeMove((byte) 7, (byte) 8, CalChessBoard.BLACK);
//
//         // 期望AI完成活四
//         byte expectedX = 3;
//         byte expectedY = 2;
//         GameAIMessageVO result = chessBoard.work();
//         System.out.println(result);
//         assertCorrectMove(result.getAIPieces(), expectedX, expectedY);
//     }
//
//
//     @Test
//     @DisplayName("测试AI在即将形成活四时是否关注对手棋盘")
//     public void testAIAttackWhenNearLiveFour() {
//         Byte[][] board = new Byte[15][15];
//         initializeBoard(board);
//         CalChessBoard chessBoard = new CalChessBoard(board, depth);
//
//         // 设置AI（白子）即将形成活四的局面
//         chessBoard.makeMove((byte) 3, (byte) 3, CalChessBoard.WHITE);
//         chessBoard.makeMove((byte) 3, (byte) 4, CalChessBoard.WHITE);
//         chessBoard.makeMove((byte) 3, (byte) 5, CalChessBoard.WHITE);
//
//         // 对手（黑子）的走棋，形成一定威胁
//         chessBoard.makeMove((byte) 7, (byte) 7, CalChessBoard.WHITE);
//         chessBoard.makeMove((byte) 7, (byte) 8, CalChessBoard.BLACK);
//         chessBoard.makeMove((byte) 7, (byte) 9, CalChessBoard.BLACK);
//         chessBoard.makeMove((byte) 7, (byte) 10, CalChessBoard.BLACK);
//         chessBoard.makeMove((byte) 7, (byte) 11, CalChessBoard.BLACK);
//
//         // 期望AI完成活四
//         // byte expectedX = 3;
//         // byte expectedY = 2;
//         // 期望AI阻止对手完成连5
//         byte expectedX = 7;
//         byte expectedY = 12;
//         GameAIMessageVO result = chessBoard.work();
//         System.out.println(result);
//         assertCorrectMove(result.getAIPieces(), expectedX, expectedY);
//     }
//
//
//
//
//     // 检查坐标，工具方法
//     private void assertCorrectMove(GameAIMessageVO.Coordinate actualMove, byte expectedX, byte expectedY) {
//         assertEquals("检查X坐标", expectedX, actualMove.getX());
//         assertEquals("检查Y坐标", expectedY, actualMove.getY());
//     }
//
//     // 初始化棋盘
//     private void initializeBoard(Byte[][] board) {
//         for (int i = 0; i < 15; i++) {
//             for (int i1 = 0; i1 < 15; i1++) {
//                 board[i][i1] = 0;
//             }
//         }
//     }
//
//
// }
