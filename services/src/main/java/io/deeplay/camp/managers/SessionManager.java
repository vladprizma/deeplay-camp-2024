package io.deeplay.camp.managers;

import TokenGenerator.TokenGenerator;
import dto.Player;
import entity.Board;
import entity.GameSession;
import entity.User;
import enums.GameStatus;
import io.deeplay.camp.handlers.MainHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manages game sessions and client handlers.
 */
public class SessionManager {
    private static SessionManager instance;
    private final List<GameSession> sessions;
    private final List<MainHandler> handlers;

    /**
     * Private constructor to prevent instantiation from other classes.
     */
    private SessionManager() {
        this.sessions = new ArrayList<>();
        this.handlers = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the SessionManager.
     *
     * @return the singleton instance
     */
    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    /**
     * Finds an existing game session or creates a new one for the provided client handler.
     *
     * @param clientHandler the client handler requesting a session
     * @return the session result containing the game session and user
     */
    public synchronized SessionResult findOrCreateSession(MainHandler clientHandler, User user) {
        for (GameSession session : sessions) {
            if (session.getPlayersCount() < 2 && session.getGameState() == GameStatus.NOT_STARTED) {
                session.setPlayer2(user);
                session.setGameState(GameStatus.IN_PROGRESS);
                return new SessionResult(session, user);
            }
        }
        GameSession newSession = new GameSession();
        newSession.setBoard(new Board());
        newSession.setPlayer1(user);
        newSession.setSessionId(TokenGenerator.generateID());
        sessions.add(newSession);
        return new SessionResult(newSession, user);
    }

    /**
     * Sends a message to the opponent of the provided client handler within the specified session.
     *
     * @param handler the client handler sending the message
     * @param session the game session
     * @param msg     the message to send
     */
    public void sendMessageToOpponent(MainHandler handler, GameSession session, String msg) {
        for (var playerHandler : handlers) {
            if (Objects.equals(playerHandler.getHandlerSession().getSessionId(), session.getSessionId())
                    && !Objects.equals(playerHandler.getHandlerPlayer().getId(), handler.getHandlerPlayer().getId())) {
                playerHandler.sendMessageToClient(msg);
            }
        }
    }

    /**
     * Adds a client handler to the list of handlers.
     *
     * @param clientHandler the client handler to add
     */
    public synchronized void addHandler(MainHandler clientHandler) {
        handlers.add(clientHandler);
    }

    /**
     * Removes a client handler from the list of handlers.
     *
     * @param clientHandler the client handler to remove
     */
    public synchronized void deleteHandler(MainHandler clientHandler) {
        handlers.remove(clientHandler);
    }

    /**
     * Gets the list of client handlers.
     *
     * @return the list of client handlers
     */
    public List<MainHandler> getHandlers() {
        return handlers;
    }
}
