//import io.deeplay.camp.board.BoardLogic;
//import entity.Board;
//import entity.User;
//import enums.Color;
//import io.deeplay.camp.game.GameLogic;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import io.deeplay.camp.user.UserService;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class PlayerServicesTest {
//
//    private Board board;
//    private UserService playerServices;
//    private Map<Integer, User> players;
//    private BoardLogic boardLogic;
//    private GameLogic gameLogic;
//
//    @BeforeEach
//    public void setUp() {
////        board = new Board();
////        playerServices = new UserService();
////        boardLogic = new BoardLogic(board);
////        players = new HashMap<>();
////        gameLogic = new GameLogic(boardLogic);
////        playerServices.addPlayer(players, 1, Color.BLACK, "", "");
////        playerServices.addPlayer(players, 2, Color.WHITE, "", "");
//    }
//
//    @Test
//    public void testAddBotAddsNewBotWhenNotPresent() {
//        String id = "123";
//        Color color = Color.BLACK;
//        assertFalse(players.containsKey(id));
//
////        assertTrue(players.containsKey(id));
////        assertTrue(players.get(id) instanceof User);
//    }
//
//    @Test
//    public void testAddBotDoesNotAtddBoWhenAlreadyPresent() {
////        String id = "123";
////        Color color = Color.BLACK;
////        players.put(Integer.parseInt(id), new Bot(id, color));
////        assertEquals(3, players.size());
//    }
//
////    @Test
////    public void testMakeMoveWithValidString() {
////        String currentPlayerId = "1";
////        String userInput = "d6";
////        boolean moveMade = gameLogic.makeMove(userInput, currentPlayerId, boardLogic);
////        assertTrue(moveMade);
////    }
//}