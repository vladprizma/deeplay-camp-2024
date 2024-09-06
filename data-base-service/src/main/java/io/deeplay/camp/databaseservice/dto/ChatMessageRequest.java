package io.deeplay.camp.databaseservice.dto;

import java.sql.Timestamp;

/**
 * Data Transfer Object for chat message requests.
 */
public class ChatMessageRequest {
    private int userId;
    private String message;
    private Timestamp timestamp;

    /**
     * Default constructor.
     */
    public ChatMessageRequest() {}

    /**
     * Constructs a new ChatMessageRequest with the specified details.
     *
     * @param userId The ID of the user sending the message.
     * @param message The content of the message.
     * @param timestamp The time when the message was sent.
     */
    public ChatMessageRequest(int userId, String message, Timestamp timestamp) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Gets the ID of the user sending the message.
     *
     * @return The ID of the user sending the message.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user sending the message.
     *
     * @param userId The ID of the user sending the message.
     */
    public void setUserId(int userId) {
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
     * @param message The content of the message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the time when the message was sent.
     *
     * @return The time when the message was sent.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the time when the message was sent.
     *
     * @param timestamp The time when the message was sent.
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}