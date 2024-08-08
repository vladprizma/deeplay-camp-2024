package io.deeplay.camp.handlers.commands;

import entity.SessionMessage;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CommandHandler for sending messages to the session chat.
 * <p>
 * This handler is responsible for processing commands to send messages to the session chat. It validates the input message,
 * adds the message to the session chat, and sends the message to all participants in the session. It also logs the process
 * and handles any unexpected errors that may occur.
 * </p>
 */
public class SendSessionChatCommandHandler implements CommandHandler {

    private static final Logger logger = Logger.getLogger(SendSessionChatCommandHandler.class.getName());

    /**
     * Handles the command to send a message to the session chat.
     * <p>
     * This method validates the input parameters, adds the message to the session chat, and sends the message to all participants
     * in the session. In case of errors, appropriate messages are sent to the client and the error is logged.
     * </p>
     *
     * @param message     The command message.
     * @param mainHandler The main handler managing the session.
     * @throws IOException            If an I/O error occurs.
     * @throws SQLException           If a SQL error occurs.
     * @throws InterruptedException   If the thread is interrupted.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        logger.info("Handling send session chat command");

        if (mainHandler.getSession() == null) {
            String errorMsg = "No active session found.";
            logger.warning(errorMsg);
            mainHandler.sendMessageToClient(errorMsg);
            return;
        }

        String[] parts = message.split(MainHandler.splitRegex, 2);
        if (parts.length < 2) {
            String errorMsg = "Invalid message format.";
            logger.warning(errorMsg);
            mainHandler.sendMessageToClient(errorMsg);
            return;
        }

        SessionManager.getInstance().sendSessionMessage(mainHandler, parts[1]);
        List<SessionMessage> sessionChat = mainHandler.getSession().getSessionChat();

        StringBuilder sb = new StringBuilder();
        for (SessionMessage messageChat : sessionChat) {
            sb.append("::").append(messageChat.getUsername()).append(" ").append(messageChat.getMsg());
        }

        String msg = "send-message-session-chat" + sb.toString();
        SessionManager.getInstance().sendMessageToAllInSession(mainHandler, msg);

        logger.info("Session chat message sent successfully.");
    }
}