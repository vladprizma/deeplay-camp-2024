package io.deeplay.camp.handlers.commands;

import enums.GameStatus;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;

public class MoveCommandHandler implements CommandHandler {

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException {
        var user = mainHandler.getUser().getId();
        
        if (user == mainHandler.getSession().getPlayer1().getId()) {
            user = 1;
        } else {
            user = 2;
        }
        
        if (mainHandler.isLogin() && mainHandler.getSession() != null) {
            boolean moveMade = mainHandler.getGameLogic().makeMove(message.split(" ")[1], user, mainHandler.getBoardLogic());
            if (moveMade) {
                mainHandler.getLogger().info("{}: Move made successfully.", mainHandler.getUser().getId());
                mainHandler.sendMessageToClient(mainHandler.getUser().getId() + ": Move made successfully.");
                mainHandler.getGameLogic().display((user == 1) ? 2 : 1, mainHandler.getBoardLogic());
                if (mainHandler.getGameLogic().checkForWin()) {
                    mainHandler.getGameLogic().displayEndGame(mainHandler.getBoardLogic());
                    mainHandler.getSession().setGameState(GameStatus.FINISHED);
                }
                mainHandler.getSession().setCurrentPlayerId((user == 1) ? mainHandler.getSession().getPlayer2().getId() : mainHandler.getSession().getPlayer1().getId());
            } else {
                mainHandler.getLogger().info("{}: Invalid move.", mainHandler.getUser().getId());
                mainHandler.sendMessageToClient(mainHandler.getUser().getId() + ": Invalid move.");
            }
        } else {
            mainHandler.sendMessageToClient("Please start a game first.");
        }
    }
}
