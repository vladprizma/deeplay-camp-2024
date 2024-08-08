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
    public synchronized SessionResult findOrCreateSession(MainHandler clientHandler, User user, boolean isBot) {
        if (isBot) {
            GameSession newSession = new GameSession();
            newSession.setBoard(new Board());
            newSession.setPlayer1(user);
            newSession.setCurrentPlayerId(user.getId());
            newSession.setSessionId(TokenGenerator.generateID());
            
            var userBot = new User(0, "Bot", "Bot", 1000, 1000, "");
            
            userBot.setIsBot(true);
            newSession.setPlayer2(userBot);
            newSession.setGameState(GameStatus.IN_PROGRESS);
            
            sessions.add(newSession);
            
            return new SessionResult(newSession, user);
        }
        
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
        newSession.setCurrentPlayerId(user.getId());
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
            if (Objects.equals(playerHandler.getSession().getSessionId(), session.getSessionId())
                    && !Objects.equals(playerHandler.getUser().getId(), handler.getUser().getId())) {
                playerHandler.sendMessageToClient(msg);
                
                break;
            }
        }
    }
    
    public void sendMessageToAll(String msg) {
        for (var playerHandler : handlers) {
            playerHandler.sendMessageToClient(msg);
        }
    }
    
    public GameSession getSession(int sessionId) {
        for (var session : sessions) {
            if (session.getSessionId() == sessionId) {
                return session;
            }
        }
        
        return null;
    }
    
    public void sendMessageToAllInSession(MainHandler mainHandler, String msg) {
        for (var handler : handlers) {
            if (handler.getSession().getSessionId() == mainHandler.getSession().getSessionId() && handler.getUser().getId() != mainHandler.getUser().getId()) {
                handler.sendMessageToClient(msg);
                mainHandler.sendMessageToClient(msg);
            }
        }
    }
    
    public synchronized void sendSessionMessage(MainHandler handler, String msg) {
        for (var session : sessions) {
            if (Objects.equals(session.getSessionId(), handler.getSession().getSessionId())) {
                var sessionMsg = new SessionMessage(msg, handler.getUser().getUsername());
                session.addMessage(sessionMsg);
                
                break;
            }
        }
    }
    
    public User getOpponent(MainHandler mainHandler) {
        for (var handler : handlers) {
            if (handler.getSession().getSessionId() == mainHandler.getSession().getSessionId() && handler.getUser().getId() != mainHandler.getUser().getId()) {
                return handler.getUser();
            }
        }
        
        return mainHandler.getUser();
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

    public synchronized void finishedSession(MainHandler clientHandler, boolean playerWon) throws SQLException {
        GameSession gameSession = null;

        for (var session : sessions) {
            if (Objects.equals(session.getSessionId(), clientHandler.getSession().getSessionId())) {
                gameSession = session;
                break;
            }
        }

        if (gameSession == null) {
            return; 
        }

        var eloService = new EloService();
        var userService = new UserService();

        if (!gameSession.getPlayer2().getIsBot()) {
            var winner = playerWon ? gameSession.getPlayer1() : gameSession.getPlayer2();
            var loser = playerWon ? gameSession.getPlayer2() : gameSession.getPlayer1();

            var eloChangedWinner = eloService.calculateEloChange(winner, loser, true);
            var eloChangedLoser = eloService.calculateEloChange(loser, winner, false);

            winner.setRating(winner.getRating() + eloChangedWinner);
            loser.setRating(Math.max(loser.getRating() - eloChangedLoser, 0));

            userService.updateRating(winner.getId(), winner.getRating());
            userService.updateRating(loser.getId(), loser.getRating());

            for (var handler : handlers) {
                if (handler.getSession().getSessionId() == (clientHandler.getSession().getSessionId())) {
                    if (handler.getUser().getId() == (winner.getId())) {
                        handler.sendMessageToClient("new-elo::" + winner.getRating());
                    } else {
                        handler.sendMessageToClient("new-elo::" + loser.getRating());
                    }
                }
            }
        } else {
            var player = gameSession.getPlayer1();
            var eloChanged = eloService.calculateEloChange(player, gameSession.getPlayer2(), playerWon);

            if (playerWon) {
                player.setRating(player.getRating() + eloChanged);
            } else {
                player.setRating(Math.max(player.getRating() - eloChanged, 0));
            }

            userService.updateRating(player.getId(), player.getRating());
            clientHandler.setUser(player);
            clientHandler.sendMessageToClient("new-elo::" + player.getRating());
        }

        sessions.remove(clientHandler.getSession());
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
