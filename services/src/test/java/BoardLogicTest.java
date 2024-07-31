//import entity.Board;
//import io.deeplay.camp.board.BoardLogic;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//
//public class BoardLogicTest {
//
//    @Test
//    public void testRemovePiece() {
//        // Create a board entity and set some chips
//        Board board = new Board();
//        board.setBlackChips(0x0000000810000000L);
//        board.setWhiteChips(0x0000001008000000L);
//
//        // Initialize the BoardLogic object with the board
//        BoardLogic boardLogic = new BoardLogic(board);
//
//        // Remove a black piece at position (1,1)
//        boardLogic.removePiece(1, 1);
//
//        // Check if the black piece was removed
//        assertFalse(boardLogic.hasPiece(1, 1));
//    }
//
//    @Test
//    public void testIsValidMove() {
//        // Create a board entity and set valid moves for both players
//        Board board = new Board();
//        BoardLogic boardLogic = new BoardLogic(board);
//        boardLogic.createValidMoves();
//
//        assertTrue(boardLogic.isValidMove(3, 2, 1));
//        assertFalse(boardLogic.isValidMove(0, 0, 1));
//        assertFalse(boardLogic.isValidMove(0, 0, 0));
//        assertTrue(boardLogic.isValidMove(4, 2, 2));
//        assertFalse(boardLogic.isValidMove(7, 7, 2));
//    }
//
//    @Test
//    public void testGetValidMoves() {
//        Board board = new Board();
//        BoardLogic boardLogic = new BoardLogic(board);
//        boardLogic.setBlackValidMoves(0x0000000000000010L);
//        boardLogic.setWhiteValidMoves(0x0000000000000100L);
//        assertEquals("0x" + Long.toHexString(0x0000000000000010L), "0x" + Long.toHexString(boardLogic.getValidMoves(1)));
//        assertEquals("0x" + Long.toHexString(0x0000000000000100L), "0x" + Long.toHexString(boardLogic.getValidMoves(2)));
//    }
//
//    @Test
//    public void testScore() {
//        Board board = new Board();
//        BoardLogic boardLogic = new BoardLogic(board);
//
//        assertArrayEquals(new int[]{2, 2}, boardLogic.score());
//    }
//
//    @Test
//    public void testGetChips() {
//        Board board = new Board();
//        BoardLogic boardLogic = new BoardLogic(board);
//        assertEquals("0x" + Long.toHexString(0x0000000810000000L), "0x" + Long.toHexString(boardLogic.getBlackChips()));
//        assertEquals("0x" + Long.toHexString(0x0000001008000000L), "0x" + Long.toHexString(boardLogic.getWhiteChips()));
//        boardLogic.setBlackChips(0x0000001008000000L);
//        boardLogic.setWhiteChips(0x0000000810000000L);
//        assertEquals("0x" + Long.toHexString(0x0000001008000000L), "0x" + Long.toHexString(boardLogic.getBlackChips()));
//        assertEquals("0x" + Long.toHexString(0x0000000810000000L), "0x" + Long.toHexString(boardLogic.getWhiteChips()));
//    }
//
//    @Test
//    public void SetPiece() {
//        Board board = new Board();
//        BoardLogic blackBoard = new BoardLogic(board); BoardLogic whiteBoard = new BoardLogic(board);
//        //Вертикали
//        blackBoard.setBlackChips(0x0000000008000000L); whiteBoard.setWhiteChips(0x0000000008000000L);
//        blackBoard.setWhiteChips(0x0000000000080000L); whiteBoard.setBlackChips(0x0000000000080000L);
//        blackBoard.createValidMoves(); whiteBoard.createValidMoves();
//        assertEquals("0x" + Long.toHexString(0x0000000000000800L), "0x" + Long.toHexString(blackBoard.getBlackValidMoves()));
//        assertEquals("0x" + Long.toHexString(0x0000000000000800L), "0x" + Long.toHexString(whiteBoard.getWhiteValidMoves()));
//        blackBoard.setPiece(3, 1, 1);
//        whiteBoard.setPiece(3, 1, 2);
//        assertEquals("0x" + Long.toHexString(0x0000000008080800L), "0x" + Long.toHexString(blackBoard.getBlackChips()));
//        assertEquals("0x" + Long.toHexString(0x0000000008080800L), "0x" + Long.toHexString(whiteBoard.getWhiteChips()));
//
//        blackBoard.setBlackChips(0x0000000008000000L); whiteBoard.setWhiteChips(0x0000000008000000L);
//        blackBoard.setWhiteChips(0x0000000800000000L); whiteBoard.setBlackChips(0x0000000800000000L);
//        blackBoard.createValidMoves(); whiteBoard.createValidMoves();
//        assertEquals("0x" + Long.toHexString(0x0000080000000000L), "0x" + Long.toHexString(blackBoard.getBlackValidMoves()));
//        assertEquals("0x" + Long.toHexString(0x0000080000000000L), "0x" + Long.toHexString(whiteBoard.getWhiteValidMoves()));
//        blackBoard.setPiece(3, 5, 1);
//        whiteBoard.setPiece(3, 5, 2);
//        assertEquals("0x" + Long.toHexString(0x0000080808000000L), "0x" + Long.toHexString(blackBoard.getBlackChips()));
//        assertEquals("0x" + Long.toHexString(0x0000080808000000L), "0x" + Long.toHexString(whiteBoard.getWhiteChips()));
//
//        //Горизонтали
//        blackBoard.setBlackChips(0x0000000008000000L); whiteBoard.setWhiteChips(0x0000000008000000L);
//        blackBoard.setWhiteChips(0x0000000004000000L); whiteBoard.setBlackChips(0x0000000004000000L);
//        blackBoard.createValidMoves(); whiteBoard.createValidMoves();
//        assertEquals("0x" + Long.toHexString(0x0000000002000000L), "0x" + Long.toHexString(blackBoard.getBlackValidMoves()));
//        assertEquals("0x" + Long.toHexString(0x0000000002000000L), "0x" + Long.toHexString(whiteBoard.getWhiteValidMoves()));
//        blackBoard.setPiece(1, 3, 1);
//        whiteBoard.setPiece(1, 3, 2);
//        assertEquals("0x" + Long.toHexString(0x000000000E000000L), "0x" + Long.toHexString(blackBoard.getBlackChips()));
//        assertEquals("0x" + Long.toHexString(0x000000000E000000L), "0x" + Long.toHexString(whiteBoard.getWhiteChips()));
//
//        blackBoard.setBlackChips(0x0000000008000000L); whiteBoard.setWhiteChips(0x0000000008000000L);
//        blackBoard.setWhiteChips(0x0000000010000000L); whiteBoard.setBlackChips(0x0000000010000000L);
//        blackBoard.createValidMoves(); whiteBoard.createValidMoves();
//        assertEquals("0x" + Long.toHexString(0x0000000020000000L), "0x" + Long.toHexString(blackBoard.getBlackValidMoves()));
//        assertEquals("0x" + Long.toHexString(0x0000000020000000L), "0x" + Long.toHexString(whiteBoard.getWhiteValidMoves()));
//        blackBoard.setPiece(5, 3, 1);
//        whiteBoard.setPiece(5, 3, 2);
//        assertEquals("0x" + Long.toHexString(0x0000000038000000L), "0x" + Long.toHexString(blackBoard.getBlackChips()));
//        assertEquals("0x" + Long.toHexString(0x0000000038000000L), "0x" + Long.toHexString(whiteBoard.getWhiteChips()));
//
//        //Диаонали
//        blackBoard.setBlackChips(0x0000000008000000L); whiteBoard.setWhiteChips(0x0000000008000000L);
//        blackBoard.setWhiteChips(0x0000000000040000L);
//        whiteBoard.setBlackChips(0x0000000000040000L);
//        blackBoard.createValidMoves();
//        whiteBoard.createValidMoves();
//        assertEquals("0x" + Long.toHexString(0x0000000000000200L), "0x" + Long.toHexString(blackBoard.getBlackValidMoves()));
//        assertEquals("0x" + Long.toHexString(0x0000000000000200L), "0x" + Long.toHexString(whiteBoard.getWhiteValidMoves()));
//        blackBoard.setPiece(1, 1, 1);
//        whiteBoard.setPiece(1, 1, 2);
//        assertEquals("0x" + Long.toHexString(0x0000000008040200L), "0x" + Long.toHexString(blackBoard.getBlackChips()));
//        assertEquals("0x" + Long.toHexString(0x0000000008040200L), "0x" + Long.toHexString(whiteBoard.getWhiteChips()));
//
//        blackBoard.setBlackChips(0x0000000008000000L); whiteBoard.setWhiteChips(0x0000000008000000L);
//        blackBoard.setWhiteChips(0x0000000000100000L);
//        whiteBoard.setBlackChips(0x0000000000100000L);
//        blackBoard.createValidMoves();
//        whiteBoard.createValidMoves();
//        assertEquals("0x" + Long.toHexString(0x0000000000002000L), "0x" + Long.toHexString(blackBoard.getBlackValidMoves()));
//        assertEquals("0x" + Long.toHexString(0x0000000000002000L), "0x" + Long.toHexString(whiteBoard.getWhiteValidMoves()));
//        blackBoard.setPiece(5, 1, 1);
//        whiteBoard.setPiece(5, 1, 2);
//        assertEquals("0x" + Long.toHexString(0x0000000008102000L), "0x" + Long.toHexString(blackBoard.getBlackChips()));
//        assertEquals("0x" + Long.toHexString(0x0000000008102000L), "0x" + Long.toHexString(whiteBoard.getWhiteChips()));
//
//        blackBoard.setBlackChips(0x0000000008000000L); whiteBoard.setWhiteChips(0x0000000008000000L);
//        blackBoard.setWhiteChips(0x0000000400000000L);
//        whiteBoard.setBlackChips(0x0000000400000000L);
//        blackBoard.createValidMoves();
//        whiteBoard.createValidMoves();
//        assertEquals("0x" + Long.toHexString(0x0000020000000000L), "0x" + Long.toHexString(blackBoard.getBlackValidMoves()));
//        assertEquals("0x" + Long.toHexString(0x0000020000000000L), "0x" + Long.toHexString(whiteBoard.getWhiteValidMoves()));
//        blackBoard.setPiece(1, 5, 1);
//        whiteBoard.setPiece(1, 5, 2);
//        assertEquals("0x" + Long.toHexString(0x0000020408000000L), "0x" + Long.toHexString(blackBoard.getBlackChips()));
//        assertEquals("0x" + Long.toHexString(0x0000020408000000L), "0x" + Long.toHexString(whiteBoard.getWhiteChips()));
//
//        blackBoard.setBlackChips(0x0000000008000000L); whiteBoard.setWhiteChips(0x0000000008000000L);
//        blackBoard.setWhiteChips(0x0000001000000000L);
//        whiteBoard.setBlackChips(0x0000001000000000L);
//        blackBoard.createValidMoves();
//        whiteBoard.createValidMoves();
//        assertEquals("0x" + Long.toHexString(0x0000200000000000L), "0x" + Long.toHexString(blackBoard.getBlackValidMoves()));
//        assertEquals("0x" + Long.toHexString(0x0000200000000000L), "0x" + Long.toHexString(whiteBoard.getWhiteValidMoves()));
//        blackBoard.setPiece(5, 5, 1);
//        whiteBoard.setPiece(5, 5, 2);
//        assertEquals("0x" + Long.toHexString(0x0000201008000000L), "0x" + Long.toHexString(blackBoard.getBlackChips()));
//        assertEquals("0x" + Long.toHexString(0x0000201008000000L), "0x" + Long.toHexString(whiteBoard.getWhiteChips()));
//    }
//
//    @Test
//    public void testGetBoardState() {
//        Board board = new Board();
//        BoardLogic boardLogic = new BoardLogic(board);
//        String expectedOutput =
//                "\n\n" +
//                        "8 . . . . . . . . \n" +
//                        "7 . . . . . . . . \n" +
//                        "6 . . . * . . . . \n" +
//                        "5 . . * 0 X . . . \n" +
//                        "4 . . . X 0 * . . \n" +
//                        "3 . . . . * . . . \n" +
//                        "2 . . . . . . . . \n" +
//                        "1 . . . . . . . . \n" +
//                        "  a b c d e f g h\n\n";
//
//        // Получаем фактический вывод
//        StringBuilder actualOutput = boardLogic.getBoardState(1);
//
//        // Проверяем, что фактический вывод соответствует ожидаемому
//        assertEquals(expectedOutput, actualOutput.toString());
//    }
//}
