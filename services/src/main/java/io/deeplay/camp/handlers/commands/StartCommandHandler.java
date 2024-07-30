package io.deeplay.camp.handlers.commands;

import enums.GameStatus;
import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.game.GameLogic;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;

public class StartCommandHandler implements CommandHandler {

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, InterruptedException {
        if (mainHandler.isLogin()) {
            var result = SessionManager.getInstance().findOrCreateSession(mainHandler, mainHandler.getUser());
            mainHandler.setSession(result.getGameSession());
            while (mainHandler.getSession().getGameState() == GameStatus.NOT_STARTED) {
                Thread.sleep(1000);
            }

            mainHandler.setBoardLogic(new BoardLogic(result.getGameSession().getBoard()));
            mainHandler.setGameLogic(new GameLogic(mainHandler.getBoardLogic()));

            mainHandler.getLogger().info("{}: The enemy was found. The game begins...", mainHandler.getUser().getId());
            mainHandler.sendMessageToClient(mainHandler.getUser().getId() + ": The enemy was found. The game begins...");

            var user = mainHandler.getUser().getId();

            if (user == mainHandler.getSession().getPlayer1().getId()) {
                user = 1;
            } else {
                user = 2;
            }


            mainHandler.getGameLogic().display(user, mainHandler.getBoardLogic());
        } else {
            mainHandler.sendMessageToClient("Please login or register.");
        }
    }
}