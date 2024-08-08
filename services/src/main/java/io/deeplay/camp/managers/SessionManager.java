package io.deeplay.camp.managers;

import TokenGenerator.TokenGenerator;
import entity.*;
import enums.GameStatus;
import io.deeplay.camp.elo.EloService;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionResult;
import io.deeplay.camp.user.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Manages game sessions and client handlers.
 */
public class SessionManager {
    private static SessionManager instance;
    private final List<GameSession> sessions;
    private final List<MainHandler> handlers;

    private SessionManager() {
        this.sessions = new ArrayList<>();
        this.handlers = new ArrayList<>();
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

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

    public void sendMessageToOpponent(MainHandler handler, GameSession session, String msg) {
        handlers.stream()
                .filter(playerHandler -> Objects.equals(playerHandler.getSession().getSessionId(), session.getSessionId()))
                .filter(playerHandler -> !Objects.equals(playerHandler.getUser().getId(), handler.getUser().getId()))
                .findFirst()
                .ifPresent(playerHandler -> playerHandler.sendMessageToClient(msg));
    }

    public void sendMessageToAll(String msg) {
        handlers.forEach(playerHandler -> playerHandler.sendMessageToClient(msg));
    }

    public GameSession getSession(int sessionId) {
        return sessions.stream()
                .filter(session -> session.getSessionId() == sessionId)
                .findFirst()
                .orElse(null);
    }

    public void sendMessageToAllInSession(MainHandler mainHandler, String msg) {
        handlers.stream()
                .filter(handler -> handler.getSession().getSessionId() == mainHandler.getSession().getSessionId())
                .filter(handler -> handler.getUser().getId() != mainHandler.getUser().getId())
                .forEach(handler -> {
                    handler.sendMessageToClient(msg);
                    mainHandler.sendMessageToClient(msg);
                });
    }

    public synchronized void sendSessionMessage(MainHandler handler, String msg) {
        sessions.stream()
                .filter(session -> Objects.equals(session.getSessionId(), handler.getSession().getSessionId()))
                .findFirst()
                .ifPresent(session -> {
                    var sessionMsg = new SessionMessage(msg, handler.getUser().getUsername());
                    session.addMessage(sessionMsg);
                });
    }

    public User getOpponent(MainHandler mainHandler) {
        return handlers.stream()
                .filter(handler -> handler.getSession().getSessionId() == mainHandler.getSession().getSessionId())
                .filter(handler -> handler.getUser().getId() != mainHandler.getUser().getId())
                .map(MainHandler::getUser)
                .findFirst()
                .orElse(mainHandler.getUser());
    }

    public synchronized void addHandler(MainHandler clientHandler) {
        handlers.add(clientHandler);
    }

    public synchronized void deleteHandler(MainHandler clientHandler) {
        handlers.remove(clientHandler);
    }

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

    public List<MainHandler> getHandlers() {
        return handlers;
    }

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

    private GameSession createNewSession(User user) {
        GameSession newSession = new GameSession();
        newSession.setBoard(new Board());
        newSession.setPlayer1(user);
        newSession.setCurrentPlayerId(user.getId());
        newSession.setSessionId(TokenGenerator.generateID());
        return newSession;
    }

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
