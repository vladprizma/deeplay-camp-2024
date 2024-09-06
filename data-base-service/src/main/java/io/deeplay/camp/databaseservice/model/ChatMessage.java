package io.deeplay.camp.databaseservice.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 * Entity class representing a chat message.
 */
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    /**
     * The unique identifier of the chat message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The user who sent the chat message.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The content of the chat message.
     */
    @Column(nullable = false)
    private String message;

    /**
     * The timestamp when the chat message was sent.
     */
    @Column(nullable = false)
    private Timestamp timestamp;

    /**
     * Default constructor.
     */
    public ChatMessage() {}

    /**
     * Constructs a new ChatMessage with the specified details.
     *
     * @param user The user who sent the chat message.
     * @param message The content of the chat message.
     * @param timestamp The timestamp when the chat message was sent.
     */
    public ChatMessage(User user, String message, Timestamp timestamp) {
        this.user = user;
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
     * Gets the user who sent the chat message.
     *
     * @return The user who sent the chat message.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who sent the chat message.
     *
     * @param user The user who sent the chat message.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the content of the chat message.
     *
     * @return The content of the chat message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the content of the chat message.
     *
     * @param message The content of the chat message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp when the chat message was sent.
     *
     * @return The timestamp when the chat message was sent.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the chat message was sent.
     *
     * @param timestamp The timestamp when the chat message was sent.
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}