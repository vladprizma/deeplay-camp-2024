package io.deeplay.camp.handlers.commands;

import enums.GameStatus;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

public class PauseCommandHandler implements CommandHandler {

    @Override
    public void handle(String message, MainHandler mainHandler) {
        if (mainHandler.getSession().getGameState() == GameStatus.IN_PROGRESS) {
            SessionManager.getInstance().sendMessageToOpponent(
                    mainHandler,
                    mainHandler.getSession(),
                    mainHandler.getUser().getId() + "pause"
            );
            mainHandler.sendMessageToClient("pause");
        } else {
            mainHandler.sendMessageToClient("Cannot pause game. Game is not in progress.");
        }
    }
}
