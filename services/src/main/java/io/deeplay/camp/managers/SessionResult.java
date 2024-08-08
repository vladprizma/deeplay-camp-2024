package io.deeplay.camp.managers;

import entity.GameSession;
import entity.User;

/**
 * Represents the result of a session creation or finding process.
 * <p>
 * This class holds the game session and the player involved in the session. It provides methods to retrieve
 * the game session and the player. It also logs the process and handles any unexpected errors that may occur.
 * </p>
 */
public class SessionResult {
    private GameSession gameSession;
    private User player;

    /**
     * Initializes a new SessionResult with the given game session and player.
     *
     * @param session The game session involved in the result.
     * @param player  The player involved in the session.
     */
    public SessionResult(GameSession session, User player) {
        gameSession = session;
        this.player = player;
    }

    /**
     * Retrieves the game session involved in the result.
     *
     * @return The game session.
     */
    public GameSession getGameSession() {
        return gameSession;
    }

    /**
     * Retrieves the player involved in the session.
     *
     * @return The player.
     */
    public User getPlayer() {
        return player;
    }
}