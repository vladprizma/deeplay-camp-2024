//import io.deeplay.camp.board.BoardLogic;
//import io.deeplay.camp.bot.BotService;
//import entity.Board;
//import entity.User;
//import io.deeplay.camp.game.GameLogic;
//
//import java.util.Map;
//
//public class BotServicesTest {
//
//    private Board board;
//    private BotService botServices;
//    private Map<String, User> players;
//    private BoardLogic boardLogic;
//    private GameLogic gameLogic;
//
////    @BeforeEach
////    public void setUp() {
////        board = new Board();
////        botServices = new BotServices();
////        boardLogic = new BoardLogic(board);
////        players = new HashMap<>();
////        gameLogic = new GameLogic(boardLogic);
////        botServices.addBot(players, "1", Color.BLACK);
////        botServices.addBot(players, "2", Color.WHITE);
////    }
////
////    @Test
////    public void testAddBotAddsNewBotWhenNotPresent() {
////        String id = "123";
////        Color color = Color.BLACK;
////        assertFalse(players.containsKey(id));
////
////        botServices.addBot(players, id, color);
////
////        assertTrue(players.containsKey(id));
////        assertTrue(players.get(id) instanceof Bot);
////    }
////
////    @Test
////    public void testAddBotDoesNotAddBotWhenAlreadyPresent() {
////        String id = "123";
////        Color color = Color.BLACK;
////        players.put(id, new Bot(id, color));
////
////        botServices.addBot(players, id, color);
////
////        assertEquals(3, players.size());
////    }
////
////    @Test
////    void testMakeMoveValidMove() {
////        String currentPlayerId = "1";
////        boolean moveMade = botServices.makeMove(currentPlayerId, boardLogic);
////
////        assertTrue(moveMade);
////    }
////
////    @Test
////    void testGetCurrentPlayerMoveValidMove() {
////        int[] move = botServices.getCurrentPlayerMove("1", boardLogic);
////
////        assertNotNull(move);
////        assertTrue(boardLogic.isValidMove(move[0], move[1], 1));
////    }
////
////    @Test
////    void testGetBotMoveReturnsValidMove() {
////        String currentPlayerId = "1";
////        int[] move = botServices.getBotMove(currentPlayerId, boardLogic);
////
////        assertNotNull(move);
////        assertTrue(move.length == 2);
////        assertTrue(move[0] >= 0 && move[0] < 8);
////        assertTrue(move[1] >= 0 && move[1] < 8);
////        assertTrue(boardLogic.isValidMove(move[0], move[1], Integer.parseInt(currentPlayerId)));
////    }
////
////    @Test
////    void testGetBotMoveReturnsMoveWithinBounds() {
////        String currentPlayerId = "1";
////        int[] move = botServices.getBotMove(currentPlayerId, boardLogic);
////
////        assertTrue(boardLogic.isValidMove(move[0], move[1], Integer.parseInt(currentPlayerId)));
////    }
//
//}