package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.dto.BoardDTO;
import io.deeplay.camp.entity.GameSession;
import io.deeplay.camp.enums.GameStatus;
import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.bot.BotStrategy;
import io.deeplay.camp.bot.RandomBot;
import io.deeplay.camp.game.GameService;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CommandHandler for processing game moves.
 * <p>
 * This handler is responsible for processing move commands from clients. It validates the session,
 * checks if it's the player's turn, processes the move, updates the game state, and sends the updated
 * board state to the client. It also handles bot moves and checks for game win conditions.
 * </p>
 */
public class MoveCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(MoveCommandHandler.class);
    private List<String> gameLogs = new ArrayList<>();

    /**
     * Handles the move command.
     * <p>
     * This method validates the session, checks if it's the player's turn, processes the move,
     * updates the game state, and sends the updated board state to the client. It also handles bot moves
     * and checks for game win conditions.
     * </p>
     *
     * @param message     the message received from the client, should not be null
     * @param mainHandler the main handler managing the session, should not be null
     * @throws IOException  if an unexpected error occurs during the handling process
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        logger.info("Handling move command");

        if (!isValidSession(mainHandler)) return;

        var session = SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId());
        initializeGameLogic(mainHandler, session);

        int playerNumber = getPlayerNumber(mainHandler, session);
        if (playerNumber == -1) return;

        if (!isPlayerTurn(mainHandler, session, playerNumber)) return;

        String move = getMoveFromMessage(message, mainHandler, session);
        if (move == null) {
            mainHandler.sendMessageToClient("Invalid move format.");
            logger.warn("Invalid move format from user {}", mainHandler.getUser().getId());
            return;
        } else if (move.equals("skip")){
            if (!session.getPlayer2().getIsBot()) {
                SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId()).setCurrentPlayerId(3 - getPlayerNumber(mainHandler, session));
                mainHandler.getGameLogic().display(3 - getPlayerNumber(mainHandler, session), mainHandler.getBoardLogic());
            }
            return;
        }

        boolean moveMade = mainHandler.getGameLogic().moveMade(mainHandler.getUser(), playerNumber, mainHandler.getBoardLogic(), move);
        if (moveMade) {
            handleSuccessfulMove(mainHandler, session, playerNumber);
        } else {
            mainHandler.sendMessageToClient(mainHandler.getUser().getId() + ": Invalid move.");
        }
    }

    /**
     * Validates the session.
     * <p>
     * This method checks if the user is logged in and if the session is valid.
     * </p>
     *
     * @param mainHandler the main handler managing the session, should not be null
     * @return true if the session is valid, false otherwise
     * @throws IOException if an unexpected error occurs during the validation process
     */
    private boolean isValidSession(MainHandler mainHandler) throws IOException {
        if (!mainHandler.isLogin() || mainHandler.getSession() == null) {
            mainHandler.sendMessageToClient("Please start a game first.");
            logger.warn("User is not logged in or session is null");
            return false;
        }

        var session = SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId());
        if (session == null) {
            mainHandler.sendMessageToClient("Session not found.");
            logger.warn("Session not found for sessionId: {}", mainHandler.getSession().getSessionId());
            return false;
        }
        return true;
    }

    /**
     * Initializes the game logic.
     * <p>
     * This method sets up the game logic and board logic for the session.
     * </p>
     *
     * @param mainHandler the main handler managing the session, should not be null
     * @param session     the game session, should not be null
     */
    private void initializeGameLogic(MainHandler mainHandler, GameSession session) {
        var newBoardLogic = new BoardService(session.getBoard());
        mainHandler.setGameLogic(new GameService(newBoardLogic));
        mainHandler.setBoardLogic(newBoardLogic);
    }

    /**
     * Retrieves the player number.
     * <p>
     * This method determines the player number based on the user ID.
     * </p>
     *
     * @param mainHandler the main handler managing the session, should not be null
     * @param session     the game session, should not be null
     * @return the player number, or -1 if the user is not a player in the session
     * @throws IOException if an unexpected error occurs during the retrieval process
     */
    private int getPlayerNumber(MainHandler mainHandler, GameSession session) throws IOException {
        var userId = mainHandler.getUser().getId();
        if (userId == session.getPlayer1().getId()) {
            return 1;
        } else if (userId == session.getPlayer2().getId()) {
            return 2;
        } else {
            mainHandler.sendMessageToClient("You are not a player in this session.");
            logger.warn("User {} is not a player in session {}", userId, session.getSessionId());
            return -1;
        }
    }

    /**
     * Checks if it's the player's turn.
     * <p>
     * This method verifies if it's the current player's turn to make a move.
     * </p>
     *
     * @param mainHandler the main handler managing the session, should not be null
     * @param session     the game session, should not be null
     * @param playerNumber the player number
     * @return true if it's the player's turn, false otherwise
     * @throws IOException if an unexpected error occurs during the check process
     */
    private boolean isPlayerTurn(MainHandler mainHandler, GameSession session, int playerNumber) throws IOException {
        var currentPlayerId = session.getCurrentPlayerId();
        var userId = mainHandler.getUser().getId();
        if (currentPlayerId != userId) {
            mainHandler.sendMessageToClient("It's not your turn.");
            logger.warn("User {} tried to make a move out of turn.", userId);
            return false;
        }
        return true;
    }

    /**
     * Extracts the move from the message.
     * <p>
     * This method parses the move command from the received message.
     * </p>
     *
     * @param message the message received from the client, should not be null
     * @return the move command, or null if the message format is invalid
     */
    private String getMoveFromMessage(String message, MainHandler mainHandler, GameSession session) throws IOException, SQLException {
        String[] messageParts = message.split(" ");
        if (messageParts.length < 2) {
            return null;
        } else if (Objects.equals(messageParts[1], "null")) {
            sendBoardStateToClient(mainHandler, session, getPlayerNumber(mainHandler, session));
            logger.info("Player " + getPlayerNumber(mainHandler, session) + " skip move");

            if (session.getPlayer2().getIsBot()) {
                handleBotMove(mainHandler, session);
            }

            return "skip";
        }
        
        return messageParts[1];
    }

    /**
     * Handles a successful move.
     * <p>
     * This method updates the session board, sends the updated board state to the client,
     * and checks for game win conditions. It also handles bot moves if applicable.
     * </p>
     *
     * @param mainHandler the main handler managing the session, should not be null
     * @param session     the game session, should not be null
     * @param playerNumber the player number
     * @throws IOException  if an unexpected error occurs during the handling process
     * @throws SQLException if a database access error occurs
     */
    private void handleSuccessfulMove(MainHandler mainHandler, GameSession session, int playerNumber) throws IOException, SQLException {
        logger.info(mainHandler.getUser().getId() + ": Move made successfully.");
        updateSessionBoard(mainHandler, session);

        sendBoardStateToClient(mainHandler, session, playerNumber);

        if (mainHandler.getGameLogic().checkForWin()) {
            handleWin(mainHandler, session);
        } else {
            session.setCurrentPlayerId(playerNumber == 1 ? session.getPlayer2().getId() : session.getPlayer1().getId());
            if (session.getPlayer2().getIsBot()) {
                handleBotMove(mainHandler, session);
            }
        }
    }

    /**
     * Updates the session board.
     * <p>
     * This method updates the board state in the session.
     * </p>
     *
     * @param mainHandler the main handler managing the session, should not be null
     * @param session     the game session, should not be null
     * @throws IOException if an unexpected error occurs during the update process
     */
    private void updateSessionBoard(MainHandler mainHandler, GameSession session) throws IOException {
        session.setBoard(mainHandler.getBoardLogic().getBoard());
        var board = session.getBoard();
        board.setBlackChips(mainHandler.getBoardLogic().getBlackChips());
        board.setWhiteChips(mainHandler.getBoardLogic().getWhiteChips());
        session.setBoard(board);

        mainHandler.getGameLogic().display(3 - getPlayerNumber(mainHandler, session), mainHandler.getBoardLogic());
    }

    /**
     * Sends the updated board state to the client.
     * <p>
     * This method sends the current board state to the client.
     * </p>
     *
     * @param mainHandler the main handler managing the session, should not be null
     * @param session     the game session, should not be null
     * @param playerNumber the player number
     * @throws IOException if an unexpected error occurs during the sending process
     */
    private void sendBoardStateToClient(MainHandler mainHandler, GameSession session, int playerNumber) throws IOException {
        var userId = mainHandler.getUser().getId();
        var boardDTO = new BoardDTO(session.getBoard());
        String boardDTOState = boardDTO.boardToClient();
        String score = mainHandler.getBoardLogic().score()[0] + " " + mainHandler.getBoardLogic().score()[1];
        String validMoves = Long.toString(mainHandler.getBoardLogic().getValidMoves(3 - playerNumber));
        var newCurrentPlayer = playerNumber == 1 ? session.getPlayer2().getId() : session.getPlayer1().getId();
        String boardState;
        
        if (session.getPlayer2().getIsBot()) {
            SessionManager.getInstance().getSession(session.getSessionId()).setCurrentPlayerId(newCurrentPlayer);
            boardState = mainHandler.getBoardLogic().getBoardStateDTO(getPlayerNumber(mainHandler, session));
        } else {
            boardState = mainHandler.getBoardLogic().getBoardStateDTO(3 - getPlayerNumber(mainHandler, session));
        }

        String msg;
        
        if (!session.getPlayer2().getIsBot()) {
            gameLogs.add("board-after-move::" + boardDTOState + "::" + score + "::" + validMoves + "::" + newCurrentPlayer);
            msg = "board-after-move::" + boardDTOState + "::" + score + "::" + validMoves + "::" + newCurrentPlayer;
        } else {
            gameLogs.add("board-after-move::" + boardState + "::" + score + "::" + validMoves + "::" + newCurrentPlayer);
            msg = "board-after-move::" + boardState + "::" + score + "::" + validMoves + "::" + newCurrentPlayer;
        }

        mainHandler.sendMessageToClient(msg);
        
        if (!session.getPlayer2().getIsBot()) {
            gameLogs.add("board-after-move::" + boardState + "::" + score + "::" + validMoves + "::" + newCurrentPlayer);
            msg = "board-after-move::" + boardState + "::" + score + "::" + validMoves + "::" + newCurrentPlayer;
            SessionManager.getInstance().sendMessageToOpponent(mainHandler, session, msg);
        }
    }

    /**
     * Handles the win condition.
     * <p>
     * This method updates the game state to finished, sends the game status to the client,
     * and finalizes the session.
     * </p>
     *
     * @param mainHandler the main handler managing the session, should not be null
     * @param session     the game session, should not be null
     * @throws IOException  if an unexpected error occurs during the handling process
     * @throws SQLException if a database access error occurs
     */
    private void handleWin(MainHandler mainHandler, GameSession session) throws IOException, SQLException {
        mainHandler.getGameLogic().displayEndGame(mainHandler.getBoardLogic());
        session.setGameState(GameStatus.FINISHED);
        String msgWin = "game-status::finished";

        if (!session.getPlayer2().getIsBot()) {
            SessionManager.getInstance().sendMessageToAllInSession(mainHandler, msgWin);
        } else {
            mainHandler.sendMessageToClient(msgWin);
        }

        boolean playerWon = getPlayerNumber(mainHandler, session) == mainHandler.getBoardLogic().checkForWin().getUserIdWinner();
        mainHandler.getSession().setLog(gameLogs);
        SessionManager.getInstance().finishedSession(mainHandler, playerWon);
    }

    /**
     * Handles the bot move.
     * <p>
     * This method executes the bot's move and updates the game state accordingly.
     * </p>
     *
     * @param mainHandler the main handler managing the session, should not be null
     * @param session     the game session, should not be null
     * @throws IOException  if an unexpected error occurs during the handling process
     * @throws SQLException if a database access error occurs
     */
    private void handleBotMove(MainHandler mainHandler, GameSession session) throws IOException, SQLException {
        BotStrategy bot = new RandomBot(2, "Bot");
        var newBoardLogicForBot = new BoardService(session.getBoard());
        mainHandler.setGameLogic(new GameService(newBoardLogicForBot));
        mainHandler.setBoardLogic(newBoardLogicForBot);

        var move = bot.getMove(bot.id, newBoardLogicForBot);
        
        if (move == null) {
            sendBoardStateToClient(mainHandler, session, bot.id);
            return;
        }
        
        newBoardLogicForBot.makeMove(bot.id, move);

        updateSessionBoard(mainHandler, session);

        sendBoardStateToClient(mainHandler, session, bot.id);

        if (mainHandler.getBoardLogic().checkForWin().isGameFinished()) {
            handleWin(mainHandler, session);
        }

        mainHandler.getGameLogic().display(1, newBoardLogicForBot);
    }
}