package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;

public class DisconnectCommandHandler implements CommandHandler {

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException {
        mainHandler.closeConnection();
    }
}
