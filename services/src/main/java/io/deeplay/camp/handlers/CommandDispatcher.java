package io.deeplay.camp.handlers;

import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    public void registerCommandHandler(String command, CommandHandler handler) {
        commandHandlers.put(command, handler);
    }

    public void dispatchCommand(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        String command = message.split(MainHandler.splitRegex)[0];
        CommandHandler handler = commandHandlers.get(command);

        if (handler != null) {
            handler.handle(message, mainHandler);
        } else {
            mainHandler.getLogger().info("Empty request or bad request: {}", message);
            mainHandler.sendMessageToClient("Empty request or bad request");
        }
    }
}
