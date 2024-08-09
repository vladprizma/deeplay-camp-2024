package io.deeplay.camp.chat;

import io.deeplay.camp.entity.ChatMessage;
import io.deeplay.camp.entity.User;
import io.deeplay.camp.dao.ChatMessageDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Service for handling chat message operations.
 * <p>
 * This class provides methods for adding, retrieving, and deleting chat messages.
 * It uses the ChatMessageDAO for database operations.
 * </p>
 */
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final ChatMessageDAO chatMessageDAO = new ChatMessageDAO();

    /**
     * Adds a new chat message to the database.
     * <p>
     * This method creates a new ChatMessage object with the given user and message, sets the current timestamp,
     * and saves it to the database using ChatMessageDAO.
     * </p>
     *
     * @param user    The user who sent the message.
     * @param message The content of the message.
     * @throws SQLException If a SQL error occurs during the operation.
     */
    public void addMessage(User user, String message) throws SQLException {
        logger.info("Adding new message from user: {}", user.getUsername());
        if (message == null || message.trim().isEmpty()) {
            logger.warn("Message content is empty");
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        ChatMessage chatMessage = new ChatMessage(0, user, message, new Timestamp(System.currentTimeMillis()));
        chatMessageDAO.addMessage(chatMessage);
    }

    /**
     * Retrieves all chat messages from the database.
     * <p>
     * This method fetches all chat messages from the database using ChatMessageDAO.
     * </p>
     *
     * @return A list of all chat messages.
     * @throws SQLException If a SQL error occurs during the operation.
     */
    public List<ChatMessage> getAllMessages() throws SQLException {
        logger.info("Retrieving all chat messages");
        return chatMessageDAO.getAllMessages();
    }

    /**
     * Deletes a chat message by its ID.
     * <p>
     * This method deletes the chat message with the given ID from the database using ChatMessageDAO.
     * </p>
     *
     * @param id The ID of the chat message to be deleted.
     * @throws SQLException If a SQL error occurs during the operation.
     */
    public void deleteMessage(int id) throws SQLException {
        logger.info("Deleting message with ID: {}", id);
        chatMessageDAO.deleteMessage(id);
    }
}