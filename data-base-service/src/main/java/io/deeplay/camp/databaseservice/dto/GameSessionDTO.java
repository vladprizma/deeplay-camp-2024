package io.deeplay.camp.databaseservice.dto;

import java.util.List;

/**
 * Data Transfer Object for game sessions.
 */
public class GameSessionDTO {
    private int id;
    private int player1Id;
    private int player2Id;
    private String result;
    private List<String> log;
    private List<String> sessionChat;

    /**
     * Default constructor.
     */
    public GameSessionDTO() {}

    /**
     * Constructs a new GameSessionDTO with the specified details.
     *
     * @param id The unique identifier of the game session.
     * @param player1Id The ID of the first player.
     * @param player2Id The ID of the second player.
     * @param result The result of the game session.
     * @param log The log of the game session.
     * @param sessionChat The chat messages of the game session.
     */
    public GameSessionDTO(int id, int player1Id, int player2Id, String result, List<String> log, List<String> sessionChat) {
        this.id = id;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
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
     * Gets the ID of the first player.
     *
     * @return The ID of the first player.
     */
    public int getPlayer1Id() {
        return player1Id;
    }

    /**
     * Sets the ID of the first player.
     *
     * @param player1Id The ID of the first player.
     */
    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    /**
     * Gets the ID of the second player.
     *
     * @return The ID of the second player.
     */
    public int getPlayer2Id() {
        return player2Id;
    }

    /**
     * Sets the ID of the second player.
     *
     * @param player2Id The ID of the second player.
     */
    public void setPlayer2Id(int player2Id) {
        this.player2Id = player2Id;
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
     * Gets the log of the game session.
     *
     * @return The log of the game session.
     */
    public List<String> getLog() {
        return log;
    }

    /**
     * Sets the log of the game session.
     *
     * @param log The log of the game session.
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