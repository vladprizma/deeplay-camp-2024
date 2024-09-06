package io.deeplay.camp.handlers.main;

import io.deeplay.camp.handlers.commands.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * CommandDispatcher is responsible for managing and dispatching commands to their respective handlers.
 * <p>
 * This class maintains a registry of command handlers and dispatches incoming commands to the appropriate handler
 * based on the command type. It also logs the process and handles any unexpected errors that may occur.
 * </p>
 */
public class CommandDispatcher {
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    /**
     * Registers a command handler for a specific command.
     * <p>
     * This method adds a command handler to the internal registry, associating it with a specific command.
     * </p>
     *
     * @param command The command to be handled.
     * @param handler The handler responsible for processing the command.
     */
    public void registerCommandHandler(String command, CommandHandler handler) {
        commandHandlers.put(command, handler);
    }

    /**
     * Dispatches a command to the appropriate handler.
     * <p>
     * This method extracts the command from the message, finds the corresponding handler in the registry,
     * and invokes the handler's handle method. If no handler is found, it logs the error and sends an appropriate
     * response to the client.
     * </p>
     *
     * @param message     The message containing the command.
     * @param mainHandler The main handler managing the session.
     * @throws IOException          If an I/O error occurs.
     * @throws SQLException         If a SQL error occurs.
     * @throws InterruptedException If the thread is interrupted.
     */
    public void dispatchCommand(String message, MainHandler mainHandler) throws Exception {
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