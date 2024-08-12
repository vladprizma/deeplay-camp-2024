import io.deeplay.camp.entity.Board;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {

    @Test
    public void testGetBlackChips() {
        // Initialize the Board object
        Board board = new Board();

        // Check if the black chips are initialized correctly
        assertEquals(0x0000000810000000L, board.getBlackChips());
    }

    @Test
    public void testGetWhiteChips() {
        // Initialize the Board object
        Board board = new Board();

        // Check if the white chips are initialized correctly
        assertEquals(0x0000001008000000L, board.getWhiteChips());
    }
}