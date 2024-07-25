import board.BoardLogic;
import display.DisplayServices;
import entity.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DisplayServicesTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private BoardLogic boardLogic;
    private DisplayServices displayServices;
    private Board board;

    @BeforeEach
    public void setUpStreams() {
        board = new Board();
        boardLogic = new BoardLogic(board);
        displayServices = new DisplayServices();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

//    @Test
//    void display_ShouldPrintBoardAndScore() {
//        String currentPlayerId = "1";
//        displayServices.display(currentPlayerId, boardLogic);
//        String output = outContent.toString();
//        assertTrue(output.contains("Score:"));
//    }

    @Test
    void displayEndGame_ShouldPrintEndGameMessage() {
        Board board = new Board();
        BoardLogic boardLogic = new BoardLogic(board);
        DisplayServices displayServices = new DisplayServices();
        displayServices.displayEndGame(boardLogic);
        String output = outContent.toString();
        assertTrue(output.contains("Black wins!") || output.contains("White wins!") || output.contains("draw"));
    }
}
