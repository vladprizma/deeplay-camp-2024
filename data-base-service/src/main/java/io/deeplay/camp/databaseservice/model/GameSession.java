package io.deeplay.camp.databaseservice.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity class representing a game session.
 */
@Entity
@Table(name = "gamesessions")
public class GameSession {

    /**
     * The unique identifier of the game session.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The first player in the game session.
     */
    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private User player1;

    /**
     * The second player in the game session.
     */
    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    private User player2;

    /**
     * The result of the game session.
     */
    @Column(nullable = false)
    private String result;

    /**
     * The log entries of the game session.
     */
    @ElementCollection
    @CollectionTable(name = "gamesession_log", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "log_entry")
    private List<String> log;

    /**
     * The chat messages of the game session.
     */
    @ElementCollection
    @CollectionTable(name = "gamesession_chat", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "chat_entry")
    private List<String> sessionChat;

    /**
     * Default constructor.
     */
    public GameSession() {}

    /**
     * Constructs a new GameSession with the specified details.
     *
     * @param player1 The first player in the game session.
     * @param player2 The second player in the game session.
     * @param result The result of the game session.
     * @param log The log entries of the game session.
     * @param sessionChat The chat messages of the game session.
     */
    public GameSession(User player1, User player2, String result, List<String> log, List<String> sessionChat) {
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
        this.log = log;
        this.sessionChat = sessionChat;
    }

    /**
     * Gets the unique identifier of the game session.
     *
     * @return The unique identifier of the game session.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the game session.
     *
     * @param id The unique identifier of the game session.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the first player in the game session.
     *
     * @return The first player in the game session.
     */
    public User getPlayer1() {
        return player1;
    }

    /**
     * Sets the first player in the game session.
     *
     * @param player1 The first player in the game session.
     */
    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    /**
     * Gets the second player in the game session.
     *
     * @return The second player in the game session.
     */
    public User getPlayer2() {
        return player2;
    }

    /**
     * Sets the second player in the game session.
     *
     * @param player2 The second player in the game session.
     */
    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    /**
     * Gets the result of the game session.
     *
     * @return The result of the game session.
     */
    public String getResult() {
        return result;
    }

    /**
     * Sets the result of the game session.
     *
     * @param result The result of the game session.
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Gets the log entries of the game session.
     *
     * @return The log entries of the game session.
     */
    public List<String> getLog() {
        return log;
    }

    /**
     * Sets the log entries of the game session.
     *
     * @param log The log entries of the game session.
     */
    public void setLog(List<String> log) {
        this.log = log;
    }

    /**
     * Gets the chat messages of the game session.
     *
     * @return The chat messages of the game session.
     */
    public List<String> getSessionChat() {
        return sessionChat;
    }

    /**
     * Sets the chat messages of the game session.
     *
     * @param sessionChat The chat messages of the game session.
     */
    public void setSessionChat(List<String> sessionChat) {
        this.sessionChat = sessionChat;
    }
}