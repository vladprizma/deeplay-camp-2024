package io.deeplay.camp.entity;

import io.deeplay.camp.enums.GameStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game session.
 * <p>
 * This class holds the state of a game session, including the current player, board, players, game state, session ID, result, log, and session chat.
 * It provides methods to get and set these details, as well as to add messages to the session chat.
 * </p>
 */
public class GameSession {
    private int currentPlayerId;
    private Board board;
    private User player1;
    private User player2;
    private GameStatus gameState = GameStatus.NOT_STARTED;
    private int sessionId;
    private String result;
    private String log;
    private List<SessionMessage> sessionChat;

    /**
     * Initializes a new GameSession with default values.
     */
    public GameSession() {
        sessionChat = new ArrayList<>();
    }

    /**
     * Initializes a new GameSession with the specified details.
     *
     * @param id      The session ID.
     * @param player1 The first player.
     * @param result  The result of the game.
     * @param player2 The second player.
     * @param log     The game log.
     */
    public GameSession(int id, User player1, String result, User player2, String log) {
        this.sessionId = id;
        this.result = result;
        this.player1 = player1;
        this.player2 = player2;
        this.log = log;
        sessionChat = new ArrayList<>();
    }

    /**
     * Gets the number of players in the session.
     *
     * @return The number of players in the session.
     */
    public int getPlayersCount() {
        int count = 0;
        if (player1 != null) count++;
        if (player2 != null) count++;
        return count;
    }

    /**
     * Gets the game log.
     *
     * @return The game log.
     */
    public String getLog() {
        return log;
    }

    /**
     * Sets the game log.
     *
     * @param log The new game log.
     */
    public void setLog(String log) {
        this.log = log;
    }

    /**
     * Gets the current game state.
     *
     * @return The current game state.
     */
    public synchronized GameStatus getGameState() {
        return gameState;
    }

    /**
     * Sets the current game state.
     *
     * @param gameState The new game state.
     */
    public synchronized void setGameState(GameStatus gameState) {
        this.gameState = gameState;
    }

    /**
     * Gets the ID of the current player.
     *
     * @return The ID of the current player.
     */
    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    /**
     * Sets the ID of the current player.
     *
     * @param currentPlayerId The new ID of the current player.
     */
    public synchronized void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    /**
     * Sets the result of the game.
     *
     * @param result The new result of the game.
     */
    public synchronized void setResult(String result) {
        this.result = result;
    }

    /**
     * Gets the result of the game.
     *
     * @return The result of the game.
     */
    public String getResult() {
        return result;
    }

    /**
     * Gets the game board.
     *
     * @return The game board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the game board.
     *
     * @param board The new game board.
     */
    public synchronized void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Gets the first player.
     *
     * @return The first player.
     */
    public User getPlayer1() {
        return player1;
    }

    /**
     * Sets the first player.
     *
     * @param player1 The new first player.
     */
    public synchronized void setPlayer1(User player1) {
        this.player1 = player1;
    }

    /**
     * Gets the second player.
     *
     * @return The second player.
     */
    public User getPlayer2() {
        return player2;
    }

    /**
     * Sets the second player.
     *
     * @param player2 The new second player.
     */
    public synchronized void setPlayer2(User player2) {
        this.player2 = player2;
    }

    /**
     * Gets the session ID.
     *
     * @return The session ID.
     */
    public synchronized int getSessionId() {
        return sessionId;
    }

    /**
     * Sets the session ID.
     *
     * @param sessionId The new session ID.
     */
    public synchronized void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Gets the session chat messages.
     *
     * @return The session chat messages.
     */
    public List<SessionMessage> getSessionChat() {
        return sessionChat;
    }

    /**
     * Sets the session chat messages.
     *
     * @param sessionChat The new session chat messages.
     */
    public synchronized void setSessionChat(List<SessionMessage> sessionChat) {
        this.sessionChat = sessionChat;
    }

    /**
     * Adds a message to the session chat.
     *
     * @param sessionMessage The message to be added.
     */
    public synchronized void addMessage(SessionMessage sessionMessage) {
        this.sessionChat.add(sessionMessage);
    }
}