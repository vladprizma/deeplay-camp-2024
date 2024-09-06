package io.deeplay.camp.entity;

import java.sql.Timestamp;

/**
 * Represents a chat message.
 * <p>
 * This class holds the details of a chat message, including the ID, user, message content, and timestamp.
 * It provides methods to get and set these details.
 * </p>
 */
public class ChatMessage {
    private int id;
    private User userId;
    private String message;
    private Timestamp timestamp;

    /**
     * Initializes a new ChatMessage with the specified details.
     *
     * @param id        The ID of the chat message.
     * @param userId    The user who sent the message.
     * @param message   The content of the message.
     * @param timestamp The timestamp when the message was sent.
     */
    public ChatMessage(int id, User userId, String message, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatMessage() {
    }
    
    /**
     * Gets the ID of the chat message.
     *
     * @return The ID of the chat message.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the chat message.
     *
     * @param id The new ID of the chat message.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user who sent the message.
     *
     * @return The user who sent the message.
     */
    public User getUserId() {
        return userId;
    }

    /**
     * Sets the user who sent the message.
     *
     * @param userId The new user who sent the message.
     */
    public void setUserId(User userId) {
        this.userId = userId;
    }

    /**
     * Gets the content of the message.
     *
     * @return The content of the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the content of the message.
     *
     * @param message The new content of the message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp when the message was sent.
     *
     * @return The timestamp when the message was sent.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the message was sent.
     *
     * @param timestamp The new timestamp when the message was sent.
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
