package io.deeplay.camp.entity;

/**
 * Represents a message in a game session.
 * <p>
 * This class holds the details of a session message, including the message content and the username of the sender.
 * It provides methods to get and set these details.
 * </p>
 */
public class SessionMessage {
    private String msg;
    private String username;

    /**
     * Initializes a new SessionMessage with the specified details.
     *
     * @param msg      The content of the message.
     * @param username The username of the sender.
     */
    public SessionMessage(String msg, String username) {
        this.msg = msg;
        this.username = username;
    }

    /**
     * Gets the content of the message.
     *
     * @return The content of the message.
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets the content of the message.
     *
     * @param msg The new content of the message.
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Gets the username of the sender.
     *
     * @return The username of the sender.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the sender.
     *
     * @param username The new username of the sender.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}