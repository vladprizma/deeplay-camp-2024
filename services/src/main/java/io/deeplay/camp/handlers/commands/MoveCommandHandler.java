package io.deeplay.camp.handlers.commands;

import dto.BoardDTO;
import enums.GameStatus;
import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.game.GameLogic;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;

public class MoveCommandHandler implements CommandHandler {

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException {
        if (!mainHandler.isLogin() || mainHandler.getSession() == null) {
            mainHandler.sendMessageToClient("Please start a game first.");
            return;
        }

        var userId = mainHandler.getUser().getId();
        var session = SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId());
        var newBoardLogic = new BoardLogic(SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId()).getBoard());
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
            return;
        }

        String move = message.split(" ")[1];
        boolean moveMade = gameLogic.moveMade(mainHandler.getUser(), playerNumber, boardLogic, move);

        if (moveMade) {
            mainHandler.getLogger().info("{}: Move made successfully.", userId);
            SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId()).setBoard(boardLogic.getBoard());
            var buff = session.getBoard();
            
            buff.setBlackChips(boardLogic.getBlackChips());
            buff.setWhiteChips(boardLogic.getWhiteChips());
            
            session.setBoard(buff);

            BoardDTO boardDTO = new BoardDTO(session.getBoard());
            String boardState = boardDTO.boardToClient();
            String msg = "board::" + userId + "::" + boardState;

            mainHandler.sendMessageToClient(msg);
            SessionManager.getInstance().sendMessageToOpponent(mainHandler, session, msg);

            gameLogic.display(playerNumber == 1 ? 2 : 1, boardLogic);
            if (gameLogic.checkForWin()) {
                gameLogic.displayEndGame(boardLogic);
                session.setGameState(GameStatus.FINISHED);
            }

            session.setCurrentPlayerId(playerNumber == 1 ? session.getPlayer2().getId() : session.getPlayer1().getId());
        } else {
            mainHandler.getLogger().info("{}: Invalid move.", userId);
            mainHandler.sendMessageToClient(userId + ": Invalid move.");
        }
    }
}
