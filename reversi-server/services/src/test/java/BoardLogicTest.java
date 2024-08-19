import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.bot.ViolettaBot;
import io.deeplay.camp.entity.Board;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardLogicTest {

    @Test
    public void testBoardLogicCopy() {
        BoardService board = new BoardService(new Board());

        ViolettaBot bot = new ViolettaBot(1, "First");
        BoardService copy = bot.getBoardCopy(board);

        String state = board.getBoardStateDTOWithoutValidMoves();
        String copyState = copy.getBoardStateDTOWithoutValidMoves();

        System.out.println(state);
        System.out.println(copyState);

        assertEquals(state, copyState);

        board.setPiece(2, 3, 1);

        state = board.getBoardStateDTOWithoutValidMoves();

        System.out.println(state);
        System.out.println(copyState);

        assertNotEquals(state, copyState);

    }
}
