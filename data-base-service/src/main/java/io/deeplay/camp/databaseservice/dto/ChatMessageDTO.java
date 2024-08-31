package io.deeplay.camp.databaseservice.dto;

import io.deeplay.camp.databaseservice.model.User;

import java.sql.Timestamp;

/**
 * Data Transfer Object for chat messages.
 */
public class ChatMessageDTO {
    private int id;
    private User userId;
    private String message;
    private Timestamp timestamp;

    /**
     * Default constructor.
     */
    public ChatMessageDTO() {}

    /**
     * Constructs a new ChatMessageDTO with the specified details.
     *
     * @param id The unique identifier of the chat message.
     * @param userId The user who sent the message.
     * @param message The content of the message.
     * @param timestamp The time when the message was sent.
     */
    public ChatMessageDTO(int id, User userId, String message, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Gets the unique identifier of the chat message.
     *
     * @return The unique identifier of the chat message.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the chat message.
     *
     * @param id The unique identifier of the chat message.
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
     * @param userId The user who sent the message.
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