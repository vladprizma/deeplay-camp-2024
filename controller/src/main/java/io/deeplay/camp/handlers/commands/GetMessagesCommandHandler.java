package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.entity.ChatMessage;
import io.deeplay.camp.chat.ChatService;
import io.deeplay.camp.handlers.main.MainHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Handler for processing get messages commands.
 * <p>
 * This handler is responsible for retrieving all chat messages and sending them to the client.
 * It ensures that the messages are correctly retrieved from the chat service and formatted
 * before being sent.
 * </p>
 */
public class GetMessagesCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetMessagesCommandHandler.class);
    private static final String MESSAGES_PREFIX = "messages";
    private final ChatService chatService = new ChatService();

    /**
     * Handles the get messages command.
     * <p>
     * This method validates the input parameters, retrieves all chat messages from the chat service,
     * formats the messages, and sends them to the client. It also logs the process and handles any
     * unexpected errors that may occur.
     * </p>
     *
     * @param message     the message received from the client, should not be null or empty
     * @param mainHandler the main handler managing the session, should not be null
     * @throws IOException  if an unexpected error occurs during the handling process
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        // Validate input parameters
        if (message == null || message.isEmpty()) {
            logger.error("Message is null or empty");
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        if (mainHandler == null) {
            logger.error("MainHandler is null");
            throw new IllegalArgumentException("MainHandler cannot be null");
        }

        logger.info("Handling get messages command for sessionId: {}", mainHandler.getSession().getSessionId());

        StringBuilder response = new StringBuilder(MESSAGES_PREFIX);

        try {
            List<ChatMessage> messages = chatService.getAllMessages();
            logger.info("Retrieved {} messages from chat service", messages.size());

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