import entity.Board;
import board.BoardLogic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BoardLogicTest {

    @Test
    public void testRemovePiece() {
        // Create a board entity and set some chips
        Board board = new Board();
        board.setBlackChips(0x0000000810000000L);
        board.setWhiteChips(0x0000001008000000L);

        // Initialize the BoardLogic object with the board
        BoardLogic boardLogic = new BoardLogic(board);

        // Remove a black piece at position (1,1)
        boardLogic.removePiece(1, 1);

        // Check if the black piece was removed
        assertFalse(boardLogic.hasPiece(1, 1));
    }

    @Test
    public void testIsValidMove() {
        // Create a board entity and set valid moves for both players
        Board board = new Board();
        BoardLogic boardLogic = new BoardLogic(board);
        boardLogic.createValidMoves();

        assertTrue(boardLogic.isValidMove(3, 2, 1));
        assertFalse(boardLogic.isValidMove(4, 2, 1));

        assertTrue(boardLogic.isValidMove(4, 2, 2));
        assertFalse(boardLogic.isValidMove(3, 2, 2));
    }

    @Test
    public void testGetValidMoves() {
        Board board = new Board();
        BoardLogic boardLogic = new BoardLogic(board);
        boardLogic.setBlackValidMoves(0x0000000000000010L);
        boardLogic.setWhiteValidMoves(0x0000000000000100L);
        assertEquals("0x" + Long.toHexString(0x0000000000000010L), "0x" + Long.toHexString(boardLogic.getValidMoves(1)));
        assertEquals("0x" + Long.toHexString(0x0000000000000100L), "0x" + Long.toHexString(boardLogic.getValidMoves(2)));
    }

    @Test
    public void testScore() {
        Board board = new Board();
        BoardLogic boardLogic = new BoardLogic(board);

        assertArrayEquals(new int[]{2, 2}, boardLogic.score());
    }

    @Test
    public void testGetChips() {
        Board board = new Board();
        BoardLogic boardLogic = new BoardLogic(board);
        assertEquals("0x" + Long.toHexString(0x0000000810000000L), "0x" + Long.toHexString(boardLogic.getBlackChips()));
        assertEquals("0x" + Long.toHexString(0x0000001008000000L), "0x" + Long.toHexString(boardLogic.getWhiteChips()));
        boardLogic.setBlackChips(0x0000001008000000L);
        boardLogic.setWhiteChips(0x0000000810000000L);
        assertEquals("0x" + Long.toHexString(0x0000001008000000L), "0x" + Long.toHexString(boardLogic.getBlackChips()));
        assertEquals("0x" + Long.toHexString(0x0000000810000000L), "0x" + Long.toHexString(boardLogic.getWhiteChips()));
    }

}