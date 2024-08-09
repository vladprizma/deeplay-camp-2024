import io.deeplay.camp.dto.BoardDTO;
import io.deeplay.camp.entity.Board;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardDTOTest {

    @Test
    public void testBoardToClientEmpty() {
        // Create an empty board entity
        Board emptyBoard = new Board();

        emptyBoard.setBlackChips(0x0000000000000000L);
        emptyBoard.setWhiteChips(0x0000000000000000L);

        // Initialize the BoardDTO object with the empty board
        BoardDTO boardDTO = new BoardDTO(emptyBoard);

        // Check if the boardToClient method returns the correct representation for an empty board
        assertEquals("Board{. . . . . . . . " +
                                   ". . . . . . . . " +
                                   ". . . . . . . . " +
                                   ". . . . . . . . " +
                                   ". . . . . . . . " +
                                   ". . . . . . . . " +
                                   ". . . . . . . . " +
                                   ". . . . . . . . }", boardDTO.boardToClient());
    }

    @Test
    public void testBoardToClientWithChips() {
        // Create a board entity with black and white chips setup
        Board board = new Board();

        // Initialize the BoardDTO object with the board containing chips
        BoardDTO boardDTO = new BoardDTO(board);

        // Check if the boardToClient method returns the correct representation for a board with chips
        assertEquals("Board{. . . . . . . . " +
                                   ". . . . . . . . " +
                                   ". . . . . . . . " +
                                   ". . . 0 X . . . " +
                                   ". . . X 0 . . . " +
                                   ". . . . . . . . " +
                                   ". . . . . . . . " +
                                   ". . . . . . . . }", boardDTO.boardToClient());
    }
}