package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.entity.SessionMessage;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Handler for processing get session chat commands.
 * <p>
 * This handler is responsible for retrieving all chat messages from the current session
 * and sending them to all clients in the session. It ensures that the messages are correctly
 * retrieved and formatted before being sent.
 * </p>
 */
public class GetSessionChatCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetSessionChatCommandHandler.class);

    /**
     * Handles the get session chat command.
     * <p>
     * This method validates the input parameters, retrieves all chat messages from the current session,
     * formats the messages, and sends them to all clients in the session. It also logs the process and
     * handles any unexpected errors that may occur.
     * </p>
     *
     * @param message     the message received from the client, should not be null
     * @param mainHandler the main handler managing the session, should not be null
     * @throws IOException          if an unexpected error occurs during the handling process
     * @throws SQLException         if a database access error occurs
     * @throws InterruptedException if the thread is interrupted while handling the command
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        // Validate input parameters
        if (message == null || mainHandler == null) {
            logger.error("Message or MainHandler is null");
            throw new IllegalArgumentException("Message or MainHandler cannot be null");
        }

        logger.info("Handling get session chat command for sessionId: {}", mainHandler.getSession().getSessionId());

        var sessionChat = mainHandler.getSession().getSessionChat();

        StringBuilder sb = new StringBuilder();
        for (SessionMessage messageChat : sessionChat) {
            sb.append("::").append(messageChat.getUsername()).append(" ").append(messageChat.getMsg());
        }

        var msg = "get-messages-session-chat" + sb.toString();

        SessionManager.getInstance().sendMessageToAllInSession(mainHandler, msg);
        logger.info("Session chat messages sent successfully");
    }
}