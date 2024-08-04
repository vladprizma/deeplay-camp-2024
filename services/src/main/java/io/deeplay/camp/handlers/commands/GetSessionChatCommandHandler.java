package io.deeplay.camp.handlers.commands;

import entity.SessionMessage;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Handler for processing get session chat commands.
 */
public class GetSessionChatCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetSessionChatCommandHandler.class);

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        // Validate input parameters
        if (message == null || mainHandler == null) {
            logger.error("Message or MainHandler is null");
            throw new IllegalArgumentException("Message or MainHandler cannot be null");
        }

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