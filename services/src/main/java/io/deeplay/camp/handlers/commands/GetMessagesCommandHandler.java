package io.deeplay.camp.handlers.commands;

import entity.ChatMessage;
import io.deeplay.camp.chat.ChatService;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.repository.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Handler for processing get messages commands.
 */
public class GetMessagesCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetMessagesCommandHandler.class);
    private final ChatService chatService = new ChatService();

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        // Validate input parameters
        if (message == null || mainHandler == null) {
            logger.error("Message or MainHandler is null");
            throw new IllegalArgumentException("Message or MainHandler cannot be null");
        }

        StringBuilder response = new StringBuilder("messages");

        try {
            List<ChatMessage> messages = chatService.getAllMessages();
            for (ChatMessage chatMessage : messages) {
                response.append("::").append(chatMessage.getTimestamp())
                        .append(" ").append(chatMessage.getUserId().getUsername())
                        .append(" ").append(chatMessage.getMessage());
            }

            mainHandler.sendMessageToClient(response.toString());
            logger.info("Messages sent to client successfully");

        } catch (SQLException e) {
            logger.error("Database error occurred while retrieving messages", e);
            mainHandler.sendMessageToClient("Internal server error. Please try again later.");
        }
    }
}