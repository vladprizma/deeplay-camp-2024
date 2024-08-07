package io.deeplay.camp.handlers.commands;

import dto.BoardDTO;
import enums.GameStatus;
import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.game.GameLogic;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * CommandHandler для обработки ходов в игре.
 */
public class MoveCommandHandler implements CommandHandler {

    private static final Logger logger = Logger.getLogger(MoveCommandHandler.class.getName());

    /**
     * Обрабатывает команду для выполнения хода.
     *
     * @param message    Сообщение команды.
     * @param mainHandler Основной обработчик, управляющий сессией.
     * @throws IOException при возникновении ошибок ввода-вывода.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        logger.info("Handling move command");

        if (!mainHandler.isLogin() || mainHandler.getSession() == null) {
            mainHandler.sendMessageToClient("Please start a game first.");
            logger.warning("User is not logged in or session is null");
            return;
        }

        var userId = mainHandler.getUser().getId();
        var session = SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId());

        if (session == null) {
            mainHandler.sendMessageToClient("Session not found.");
            logger.warning("Session not found for sessionId: " + mainHandler.getSession().getSessionId());
            return;
        }

        var newBoardLogic = new BoardLogic(session.getBoard());
        mainHandler.setGameLogic(new GameLogic(newBoardLogic));
        mainHandler.setBoardLogic(newBoardLogic);
        var gameLogic = mainHandler.getGameLogic();
        var boardLogic = mainHandler.getBoardLogic();

        int playerNumber;
        if (userId == session.getPlayer1().getId()) {
            playerNumber = 1;
        } else if (userId == session.getPlayer2().getId()) {
            playerNumber = 2;
        } else {
            mainHandler.sendMessageToClient("You are not a player in this session.");
            logger.warning("User " + userId + " is not a player in session " + session.getSessionId());
            return;
        }

        String[] messageParts = message.split(" ");
        if (messageParts.length < 2) {
            mainHandler.sendMessageToClient("Invalid move format.");
            logger.warning("Invalid move format from user " + userId);
            return;
        }

        String move = messageParts[1];
        boolean moveMade = gameLogic.moveMade(mainHandler.getUser(), playerNumber, boardLogic, move);

        if (moveMade) {
            logger.info(userId + ": Move made successfully.");
            session.setBoard(boardLogic.getBoard());
            var newCurrentPlayer = playerNumber == 1 ? session.getPlayer2().getId() : session.getPlayer1().getId();
            var buff = session.getBoard();
            buff.setBlackChips(boardLogic.getBlackChips());
            buff.setWhiteChips(boardLogic.getWhiteChips());
            session.setBoard(buff);

            BoardDTO boardDTO = new BoardDTO(session.getBoard());
            String boardState = boardDTO.boardToClient();
            String score = Integer.toString(boardLogic.score()[0]) + " " + Integer.toString(boardLogic.score()[1]);
            String validMoves = Long.toString(boardLogic.getValidMoves(3 - playerNumber));
            String msg = "board-after-move::" + userId + "::" + boardState + "::" + score + "::" + validMoves + "::" + newCurrentPlayer;

            mainHandler.sendMessageToClient(msg);
            
            if (!SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId()).getPlayer2().getIsBot()) SessionManager.getInstance().sendMessageToOpponent(mainHandler, session, msg);

            gameLogic.display(playerNumber == 1 ? 2 : 1, boardLogic);
            if (gameLogic.checkForWin()) {
                gameLogic.displayEndGame(boardLogic);
                session.setGameState(GameStatus.FINISHED);
                String msgWin = "game-status::finished";

                if (!SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId()).getPlayer2().getIsBot()) SessionManager.getInstance().sendMessageToAllInSession(mainHandler, msgWin);
                else mainHandler.sendMessageToClient(msgWin);
                
                boolean playerWon = boardLogic.score()[0] > boardLogic.score()[1];
                
                SessionManager.getInstance().finishedSession(mainHandler, playerWon);
            }

            session.setCurrentPlayerId(playerNumber == 1 ? session.getPlayer2().getId() : session.getPlayer1().getId());
            
            //логика хода бота
            if (SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId()).getPlayer2().getIsBot()) {
                mainHandler.sendMessageToClient("Я бот и меня надо писать, да. Я в файле MoveCommandHandler на 112 строке. Тут я должен походить.");
            }
        } else {
            logger.info(userId + ": Invalid move.");
            mainHandler.sendMessageToClient(userId + ": Invalid move.");
        }
    }
}