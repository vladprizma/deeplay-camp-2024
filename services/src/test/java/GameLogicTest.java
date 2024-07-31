import io.deeplay.camp.board.BoardLogic;
import entity.Board;
import enums.GameStatus;
import io.deeplay.camp.game.GameLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {

    private Board board;
    private GameLogic gameLogic;
    private BoardLogic boardLogic;

    @BeforeEach
    void setUp() {
        board = new Board();
        boardLogic = new BoardLogic(board);
        gameLogic = new GameLogic(boardLogic);
    }

//    @Test
//    void setCurrentPlayer_ShouldReturnPlayerId_WhenPlayerExists() {
//        int playerId = 12;
//        Map<String, User> players = new HashMap<>();
//        players.put(Integer.toString(playerId), new User(playerId, "", "", 1, 1, ""));
//
//        String result = gameLogic.setCurrentPlayer(Integer.toString(playerId), players);
//
////        assertEquals(playerId, result);
//    }
//
//    @Test
//    void setCurrentPlayer_ShouldThrowException_WhenPlayerDoesNotExist() {
//        String playerId = "nonExistingPlayer";
//        Map<String, User> players = new HashMap<>();
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            gameLogic.setCurrentPlayer(playerId, players);
//        });
//
//        String expectedMessage = "Player with ID " + playerId + " does not exist.";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @Test
//    void moveSkipped_ShouldAlwaysReturnFalse() {
//        assertFalse(gameLogic.moveSkipped("anyPlayerId"));
//    }

    @Test
    void gameFinished_ShouldReturnFinishedStatus() {
        assertEquals(GameStatus.FINISHED, gameLogic.gameFinished());
    }

    @Test
    void gameStarted_ShouldReturnInProgressStatus() {
        assertEquals(GameStatus.IN_PROGRESS, gameLogic.gameStarted());
    }

    @Test
    void gameStopped_ShouldReturnPausedStatus() {
        assertEquals(GameStatus.PAUSED, gameLogic.gameStopped());
    }

    @Test
    void gameResumed_ShouldReturnInProgressStatus() {
        assertEquals(GameStatus.IN_PROGRESS, gameLogic.gameResumed());
    }

//    @Test
//    void scoreUpdated_ShouldAlwaysReturnFalse() {
//        assertFalse(gameLogic.scoreUpdated());
//    }

//    @Test
//    void playerTurn_ShouldTogglePlayers() {
//        Map<String, User> players = new HashMap<>();
//        players.put("1", new User(1,  "", "", 1, 1, ""));
//        players.put("2", new User(2, "", "", 1, 1, ""));
//
//        assertEquals("2", gameLogic.playerTurn("1", players));
//        assertEquals("1", gameLogic.playerTurn("2", players));
//    }
}
