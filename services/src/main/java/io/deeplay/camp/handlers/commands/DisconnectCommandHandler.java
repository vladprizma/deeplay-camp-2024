package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Handler for processing disconnect commands.
 */
public class DisconnectCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(DisconnectCommandHandler.class);

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException {
        // Validate input parameters
        if (mainHandler == null) {
            logger.error("MainHandler is null");
            throw new IllegalArgumentException("MainHandler cannot be null");
        }

        try {
            // Close the connection with the client
            mainHandler.closeConnection();
            logger.info("Connection closed for sessionId: {}", mainHandler.getSession().getSessionId());

            // Notify the opponent about the disconnection
            SessionManager.getInstance().sendMessageToOpponent(mainHandler, mainHandler.getSession(), "opponent-disconnect");
            logger.info("Opponent notified about disconnection for sessionId: {}", mainHandler.getSession().getSessionId());

        } catch (Exception e) {
            logger.error("Unexpected error occurred while handling disconnect command", e);
            throw new IOException("Unexpected error occurred", e);
        }
    }
}