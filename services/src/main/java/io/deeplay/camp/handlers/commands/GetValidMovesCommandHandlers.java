package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;

public class GetValidMovesCommandHandlers implements CommandHandler {
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        var boardLogic = mainHandler.getBoardLogic();

        var user = mainHandler.getUser().getId();

        if (user == mainHandler.getSession().getPlayer1().getId()) {
            user = 1;
        } else {
            user = 2;
        }
        
        var msg = boardLogic.getValidMoves(user);
        
        mainHandler.sendMessageToClient("get-valid-moves::" + msg);
    }
}
