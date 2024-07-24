package managers;

import TokenGenerator.TokenGenerator;
import entity.GameSession;
import entity.Player;
import enums.Color;
import enums.GameStatus;
import handlers.ClientHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manages game sessions and client handlers.
 */
public class SessionManager {
    private static SessionManager instance;
    private final List<GameSession> sessions;
    private final List<ClientHandler> handlers;

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
     * @return the session result containing the game session and player
     */
    public synchronized SessionResult findOrCreateSession(ClientHandler clientHandler) {
        for (GameSession session : sessions) {
            if (session.getPlayersCount() < 2 && session.getGameState() == GameStatus.NOT_STARTED) {
                Player player2 = new Player(TokenGenerator.generateID(), Color.BLACK, "asd", "asd");
                session.setPlayer2(player2);
                session.setGameState(GameStatus.IN_PROGRESS);
                return new SessionResult(session, player2);
            }
        }
        GameSession newSession = new GameSession();
        Player player1 = new Player(TokenGenerator.generateID(), Color.WHITE, "asd", "asd");
        newSession.setPlayer1(player1);
        newSession.setSessionId(TokenGenerator.generateID());
        sessions.add(newSession);
        return new SessionResult(newSession, player1);
    }

    /**
     * Sends a message to the opponent of the provided client handler within the specified session.
     *
     * @param handler the client handler sending the message
     * @param session the game session
     * @param msg     the message to send
     */
    public void sendMessageToOpponent(ClientHandler handler, GameSession session, String msg) {
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
    public synchronized void addHandler(ClientHandler clientHandler) {
        handlers.add(clientHandler);
    }

    /**
     * Removes a client handler from the list of handlers.
     *
     * @param clientHandler the client handler to remove
     */
    public synchronized void deleteHandler(ClientHandler clientHandler) {
        handlers.remove(clientHandler);
    }

    /**
     * Gets the list of client handlers.
     *
     * @return the list of client handlers
     */
    public List<ClientHandler> getHandlers() {
        return handlers;
    }
}
