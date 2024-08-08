package io.deeplay.camp.handlers.commands;

import dto.BoardDTO;
import entity.GameSession;
import enums.GameStatus;
import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.bot.BotStrategy;
import io.deeplay.camp.bot.RandomBot;
import io.deeplay.camp.game.GameLogic;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * CommandHandler for processing game moves.
 * <p>
 * This handler is responsible for processing move commands from clients. It validates the session,
 * checks if it's the player's turn, processes the move, updates the game state, and sends the updated
 * board state to the client. It also handles bot moves and checks for game win conditions.
 * </p>
 */
public class MoveCommandHandler implements CommandHandler {

    private static final Logger logger = Logger.getLogger(MoveCommandHandler.class.getName());

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

        String move = getMoveFromMessage(message);
        if (move == null) {
            mainHandler.sendMessageToClient("Invalid move format.");
            logger.warning("Invalid move format from user " + mainHandler.getUser().getId());
            return;
        }

        boolean moveMade = mainHandler.getGameLogic().moveMade(mainHandler.getUser(), playerNumber, mainHandler.getBoardLogic(), move);
        if (moveMade) {
            handleSuccessfulMove(mainHandler, session, playerNumber);
        } else {
            mainHandler.sendMessageToClient(mainHandler.getUser().getId() + ": Invalid move.");
        }
    }

    private boolean isValidSession(MainHandler mainHandler) throws IOException {
        if (!mainHandler.isLogin() || mainHandler.getSession() == null) {
            mainHandler.sendMessageToClient("Please start a game first.");
            logger.warning("User is not logged in or session is null");
            return false;
        }

        var session = SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId());
        if (session == null) {
            mainHandler.sendMessageToClient("Session not found.");
            logger.warning("Session not found for sessionId: " + mainHandler.getSession().getSessionId());
            return false;
        }
        return true;
    }

    private void initializeGameLogic(MainHandler mainHandler, GameSession session) {
        var newBoardLogic = new BoardLogic(session.getBoard());
        mainHandler.setGameLogic(new GameLogic(newBoardLogic));
        mainHandler.setBoardLogic(newBoardLogic);
    }

    private int getPlayerNumber(MainHandler mainHandler, GameSession session) throws IOException {
        var userId = mainHandler.getUser().getId();
        if (userId == session.getPlayer1().getId()) {
            return 1;
        } else if (userId == session.getPlayer2().getId()) {
            return 2;
        } else {
            mainHandler.sendMessageToClient("You are not a player in this session.");
            logger.warning("User " + userId + " is not a player in session " + session.getSessionId());
            return -1;
        }
    }

    private boolean isPlayerTurn(MainHandler mainHandler, GameSession session, int playerNumber) throws IOException {
        var currentPlayerId = session.getCurrentPlayerId();
        var userId = mainHandler.getUser().getId();
        if (currentPlayerId != userId) {
            mainHandler.sendMessageToClient("It's not your turn.");
            logger.warning("User " + userId + " tried to make a move out of turn.");
            return false;
        }
        return true;
    }

    private String getMoveFromMessage(String message) {
        String[] messageParts = message.split(" ");
        if (messageParts.length < 2) {
            return null;
        }
        return messageParts[1];
    }

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

    private void updateSessionBoard(MainHandler mainHandler, GameSession session) throws IOException {
        session.setBoard(mainHandler.getBoardLogic().getBoard());
        var board = session.getBoard();
        board.setBlackChips(mainHandler.getBoardLogic().getBlackChips());
        board.setWhiteChips(mainHandler.getBoardLogic().getWhiteChips());
        session.setBoard(board);

        mainHandler.getGameLogic().display(3 - getPlayerNumber(mainHandler, session), mainHandler.getBoardLogic());
    }

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
            msg = "board-after-move::" + boardDTOState + "::" + score + "::" + validMoves + "::" + newCurrentPlayer;
        } else {
            msg = "board-after-move::" + boardState + "::" + score + "::" + validMoves + "::" + newCurrentPlayer;
        }

        mainHandler.sendMessageToClient(msg);
        if (!session.getPlayer2().getIsBot()) {
            msg = "board-after-move::" + boardState + "::" + score + "::" + validMoves + "::" + newCurrentPlayer;
            SessionManager.getInstance().sendMessageToOpponent(mainHandler, session, msg);
        }
    }

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
        SessionManager.getInstance().finishedSession(mainHandler, playerWon);
    }

    private void handleBotMove(MainHandler mainHandler, GameSession session) throws IOException, SQLException {
        BotStrategy bot = new RandomBot(2, "Bot");
        var newBoardLogicForBot = new BoardLogic(session.getBoard());
        mainHandler.setGameLogic(new GameLogic(newBoardLogicForBot));
        mainHandler.setBoardLogic(newBoardLogicForBot);

        newBoardLogicForBot.makeMove(bot.id, bot.getMakeMove(bot.id, newBoardLogicForBot));

        updateSessionBoard(mainHandler, session);

        sendBoardStateToClient(mainHandler, session, bot.id);

        if (mainHandler.getBoardLogic().checkForWin().isGameFinished()) {
            handleWin(mainHandler, session);
        }

        mainHandler.getGameLogic().display(1, newBoardLogicForBot);
    }
}