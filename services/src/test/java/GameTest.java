import board.BoardLogic;
import bot.BotServices;
import entity.Board;
import entity.Player;
import enums.Color;
import enums.GameStatus;
import game.Game;
import game.GameLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private BotServices botServices;
    private GameLogic gameLogic;
    private Board board;
    private BoardLogic boardLogic;
    private Map<String, Player> players = new HashMap<>();

    @BeforeEach
    void setUp() {
        botServices = new BotServices();
        board = new Board();
        boardLogic = new BoardLogic(board);
        gameLogic = new GameLogic(boardLogic);
        botServices.addBot(players, "1", Color.BLACK);
        botServices.addBot(players, "2", Color.WHITE);
        game = new Game();
    }

    @Test
    void testGameInFinished() {
        game.playGame();
        assertEquals(GameStatus.FINISHED, game.getGameState());
    }
}
