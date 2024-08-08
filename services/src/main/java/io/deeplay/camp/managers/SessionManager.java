package io.deeplay.camp.managers;

import TokenGenerator.TokenGenerator;
import entity.*;
import enums.GameStatus;
import io.deeplay.camp.elo.EloService;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.user.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Manages game sessions and client handlers.
 * <p>
 * This singleton class is responsible for creating, managing, and terminating game sessions. It also handles
 * communication between clients and manages the ELO rating updates for players.
 * </p>
 */
public class SessionManager {
    private static SessionManager instance;
    private final List<GameSession> sessions;
    private final List<MainHandler> handlers;

    private SessionManager() {
        this.sessions = new ArrayList<>();
        this.handlers = new ArrayList<>();
    }

    /**
     * Retrieves the singleton instance of the SessionManager.
     *
     * @return The singleton instance of the SessionManager.
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    /**
     * Finds an available session or creates a new one for the given user.
     * <p>
     * If the user is a bot, a new session with a bot is created. Otherwise, it tries to find an available session
     * or creates a new one if none are available.
     * </p>
     *
     * @param clientHandler The handler managing the client connection.
     * @param user          The user for whom the session is being created or found.
     * @param isBot         Indicates if the user is a bot.
     * @return The result of the session creation or finding process.
     */
    public synchronized SessionResult findOrCreateSession(MainHandler clientHandler, User user, boolean isBot) {
        if (isBot) {
            return createBotSession(user);
        }

        Optional<GameSession> availableSession = sessions.stream()
                .filter(session -> session.getPlayersCount() < 2 && session.getGameState() == GameStatus.NOT_STARTED)
                .findFirst();

        if (availableSession.isPresent()) {
            GameSession session = availableSession.get();
            session.setPlayer2(user);
            session.setGameState(GameStatus.IN_PROGRESS);
            return new SessionResult(session, user);
        }

        GameSession newSession = createNewSession(user);
        sessions.add(newSession);
        return new SessionResult(newSession, user);
    }

    /**
     * Sends a message to the opponent of the given handler in the specified session.
     *
     * @param handler  The handler sending the message.
     * @param session  The session in which the message is being sent.
     * @param msg      The message to be sent.
     */
    public void sendMessageToOpponent(MainHandler handler, GameSession session, String msg) {
        handlers.stream()
                .filter(playerHandler -> Objects.equals(playerHandler.getSession().getSessionId(), session.getSessionId()))
                .filter(playerHandler -> !Objects.equals(playerHandler.getUser().getId(), handler.getUser().getId()))
                .findFirst()
                .ifPresent(playerHandler -> playerHandler.sendMessageToClient(msg));
    }

    /**
     * Sends a message to all connected handlers.
     *
     * @param msg The message to be sent.
     */
    public void sendMessageToAll(String msg) {
        handlers.forEach(playerHandler -> playerHandler.sendMessageToClient(msg));
    }

    /**
     * Retrieves a session by its ID.
     *
     * @param sessionId The ID of the session to be retrieved.
     * @return The session with the specified ID, or null if not found.
     */
    public GameSession getSession(int sessionId) {
        return sessions.stream()
                .filter(session -> session.getSessionId() == sessionId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Sends a message to all handlers in the same session as the given handler.
     *
     * @param mainHandler The handler sending the message.
     * @param msg         The message to be sent.
     */
    public void sendMessageToAllInSession(MainHandler mainHandler, String msg) {
        handlers.stream()
                .filter(handler -> handler.getSession().getSessionId() == mainHandler.getSession().getSessionId())
                .filter(handler -> handler.getUser().getId() != mainHandler.getUser().getId())
                .forEach(handler -> {
                    handler.sendMessageToClient(msg);
                    mainHandler.sendMessageToClient(msg);
                });
    }

    /**
     * Sends a session message from the given handler.
     *
     * @param handler The handler sending the message.
     * @param msg     The message to be sent.
     */
    public synchronized void sendSessionMessage(MainHandler handler, String msg) {
        sessions.stream()
                .filter(session -> Objects.equals(session.getSessionId(), handler.getSession().getSessionId()))
                .findFirst()
                .ifPresent(session -> {
                    var sessionMsg = new SessionMessage(msg, handler.getUser().getUsername());
                    session.addMessage(sessionMsg);
                });
    }

    /**
     * Retrieves the opponent of the given handler in the current session.
     *
     * @param mainHandler The handler whose opponent is to be retrieved.
     * @return The opponent user, or the user of the given handler if no opponent is found.
     */
    public User getOpponent(MainHandler mainHandler) {
        return handlers.stream()
                .filter(handler -> handler.getSession().getSessionId() == mainHandler.getSession().getSessionId())
                .filter(handler -> handler.getUser().getId() != mainHandler.getUser().getId())
                .map(MainHandler::getUser)
                .findFirst()
                .orElse(mainHandler.getUser());
    }

    /**
     * Adds a handler to the list of active handlers.
     *
     * @param clientHandler The handler to be added.
     */
    public synchronized void addHandler(MainHandler clientHandler) {
        handlers.add(clientHandler);
    }

    /**
     * Removes a handler from the list of active handlers.
     *
     * @param clientHandler The handler to be removed.
     */
    public synchronized void deleteHandler(MainHandler clientHandler) {
        handlers.remove(clientHandler);
    }

    /**
     * Finishes a session and updates the ELO ratings of the players.
     *
     * @param clientHandler The handler managing the client connection.
     * @param playerWon     Indicates if the player won the session.
     * @throws SQLException If a SQL error occurs during the ELO update.
     */
    public synchronized void finishedSession(MainHandler clientHandler, boolean playerWon) throws SQLException {
        GameSession gameSession = sessions.stream()
                .filter(session -> Objects.equals(session.getSessionId(), clientHandler.getSession().getSessionId()))
                .findFirst()
                .orElse(null);

        if (gameSession == null) {
            return;
        }

        var eloService = new EloService();
        var userService = new UserService();

        if (!gameSession.getPlayer2().getIsBot()) {
            updateEloForPlayers(gameSession, playerWon, eloService, userService);
            notifyPlayersAboutNewElo(clientHandler, gameSession);
        } else {
            updateEloForPlayerVsBot(gameSession, playerWon, eloService, userService, clientHandler);
        }

        sessions.remove(gameSession);
    }

    /**
     * Retrieves the list of active handlers.
     *
     * @return The list of active handlers.
     */
    public List<MainHandler> getHandlers() {
        return handlers;
    }

    /**
     * Creates a new session with a bot.
     *
     * @param user The user for whom the session is being created.
     * @return The result of the session creation process.
     */
    private SessionResult createBotSession(User user) {
        GameSession newSession = new GameSession();
        newSession.setBoard(new Board());
        newSession.setPlayer1(user);
        newSession.setCurrentPlayerId(user.getId());
        newSession.setSessionId(TokenGenerator.generateID());

        User userBot = new User(0, "Bot", "Bot", 1000, 1000, "");
        userBot.setIsBot(true);
        newSession.setPlayer2(userBot);
        newSession.setGameState(GameStatus.IN_PROGRESS);

        sessions.add(newSession);
        return new SessionResult(newSession, user);
    }

    /**
     * Creates a new session for the given user.
     *
     * @param user The user for whom the session is being created.
     * @return The newly created game session.
     */
    private GameSession createNewSession(User user) {
        GameSession newSession = new GameSession();
        newSession.setBoard(new Board());
        newSession.setPlayer1(user);
        newSession.setCurrentPlayerId(user.getId());
        newSession.setSessionId(TokenGenerator.generateID());
        return newSession;
    }

    /**
     * Updates the ELO ratings for both players in a session.
     *
     * @param gameSession The game session.
     * @param playerWon   Indicates if the player won the session.
     * @param eloService  The ELO service for calculating ELO changes.
     * @param userService The user service for updating user ratings.
     * @throws SQLException If a SQL error occurs during the ELO update.
     */
    private void updateEloForPlayers(GameSession gameSession, boolean playerWon, EloService eloService, UserService userService) throws SQLException {
        User winner = playerWon ? gameSession.getPlayer1() : gameSession.getPlayer2();
        User loser = playerWon ? gameSession.getPlayer2() : gameSession.getPlayer1();

        int eloChangedWinner = eloService.calculateEloChange(winner, loser, true);
        int eloChangedLoser = eloService.calculateEloChange(loser, winner, false);

        winner.setRating(winner.getRating() + eloChangedWinner);
        loser.setRating(Math.max(loser.getRating() - eloChangedLoser, 0));

        userService.updateRating(winner.getId(), winner.getRating());
        userService.updateRating(loser.getId(), loser.getRating());
    }

    /**
     * Notifies players about their new ELO ratings.
     *
     * @param clientHandler The handler managing the client connection.
     * @param gameSession   The game session.
     */
    private void notifyPlayersAboutNewElo(MainHandler clientHandler, GameSession gameSession) {
        handlers.stream()
                .filter(handler -> handler.getSession().getSessionId() == clientHandler.getSession().getSessionId())
                .forEach(handler -> {
                    if (handler.getUser().getId() == gameSession.getPlayer1().getId()) {
                        handler.sendMessageToClient("new-elo::" + gameSession.getPlayer1().getRating());
                    } else {
                        handler.sendMessageToClient("new-elo::" + gameSession.getPlayer2().getRating());
                    }
                });
    }

    /**
     * Updates the ELO rating for a player in a session against a bot.
     *
     * @param gameSession   The game session.
     * @param playerWon     Indicates if the player won the session.
     * @param eloService    The ELO service for calculating ELO changes.
     * @param userService   The user service for updating user ratings.
     * @param clientHandler The handler managing the client connection.
     * @throws SQLException If a SQL error occurs during the ELO update.
     */
    private void updateEloForPlayerVsBot(GameSession gameSession, boolean playerWon, EloService eloService, UserService userService, MainHandler clientHandler) throws SQLException {
        User player = gameSession.getPlayer1();
        int eloChanged = eloService.calculateEloChange(player, gameSession.getPlayer2(), playerWon);

        if (playerWon) {
            player.setRating(player.getRating() + eloChanged);
        } else {
            player.setRating(Math.max(player.getRating() - eloChanged, 0));
        }

        userService.updateRating(player.getId(), player.getRating());
        clientHandler.setUser(player);
        clientHandler.sendMessageToClient("new-elo::" + player.getRating());
    }
}