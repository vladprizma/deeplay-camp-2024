package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Handler for processing disconnect commands.
 * <p>
 * This handler is responsible for managing the disconnection process of a client.
 * It ensures that the connection is properly closed and the opponent is notified
 * about the disconnection.
 * </p>
 */
public class DisconnectCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(DisconnectCommandHandler.class);
    private static final String OPPONENT_DISCONNECT_MESSAGE = "opponent-disconnect";

    /**
     * Handles the disconnect command.
     * <p>
     * This method validates the input parameters, closes the connection with the client,
     * and notifies the opponent about the disconnection. It also logs the process and
     * handles any unexpected errors that may occur.
     * </p>
     *
     * @param message     the message received from the client, should not be null or empty
     * @param mainHandler the main handler managing the session, should not be null
     * @throws IOException if an unexpected error occurs during the handling process
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException {
        // Validate input parameters
        if (message == null || message.isEmpty()) {
            logger.error("Message is null or empty");
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        if (mainHandler == null) {
            logger.error("MainHandler is null");
            throw new IllegalArgumentException("MainHandler cannot be null");
        }

        logger.info("Handling disconnect command for sessionId: {}", mainHandler.getSession().getSessionId());

        try {
            // Close the connection with the client
            mainHandler.closeConnection();
            logger.info("Connection closed for sessionId: {}", mainHandler.getSession().getSessionId());

            // Notify the opponent about the disconnection
            SessionManager.getInstance().sendMessageToOpponent(mainHandler, mainHandler.getSession(), OPPONENT_DISCONNECT_MESSAGE);
            logger.info("Opponent notified about disconnection for sessionId: {}", mainHandler.getSession().getSessionId());

        } catch (Exception e) {
            logger.error("Unexpected error occurred while handling disconnect command", e);
            throw new IOException("Unexpected error occurred", e);
        }
    }
}