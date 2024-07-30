package io.deeplay.camp.handlers.commands;

import enums.GameStatus;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;

public class StartCommandHandler implements CommandHandler {

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, InterruptedException {
        if (mainHandler.isLogin()) {
            var result = SessionManager.getInstance().findOrCreateSession(mainHandler);
            mainHandler.setSession(result.getGameSession());
            while (mainHandler.getSession().getGameState() == GameStatus.NOT_STARTED) {
                Thread.sleep(1000);
            }

            mainHandler.getLogger().info(mainHandler.getUser().getId() + ": The enemy was found. The game begins...");
            mainHandler.sendMessageToClient(mainHandler.getUser().getId() + ": The enemy was found. The game begins...");
        } else {
            mainHandler.sendMessageToClient("Please login or register.");
        }
    }
}