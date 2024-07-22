package managers;

import entity.GameSession;
import entity.Player;
import enums.Color;
import enums.GameStatus;
import handlers.ClientHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SessionManager {
    private static SessionManager instance;
    private final List<GameSession> sessions;
    private final List<ClientHandler> handlers;
    
    private SessionManager() {
        this.sessions = new ArrayList<>();
        this.handlers = new ArrayList<>();
    }
    
    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }
    
    public synchronized SessionResult findOrCreateSession(ClientHandler clientHandler) {
        for (GameSession session : sessions) {
            if (session.getPlayersCount() < 2 && session.gameState == GameStatus.NOT_STARTED) {
                session.player2 = new Player("2", Color.BLACK);
                session.gameState = GameStatus.IN_PROGRESS;
                return new SessionResult(session, session.player2);
            }
        }
        GameSession newSession = new GameSession();
        newSession.player1 = new Player("1", Color.WHITE);
        newSession.sessionId = "1";
        sessions.add(newSession);
        return new SessionResult(newSession, newSession.player1);
    }
    
    public void sendMessageToOpponent(ClientHandler handler, GameSession session, String msg) {
        for(var playerHandler : handlers) {
            if (Objects.equals(playerHandler.getHandlerSession().sessionId, session.sessionId) 
                    && !Objects.equals(playerHandler.getHandlerPlayer().getId(), handler.getHandlerPlayer().getId())) {
                playerHandler.sendMessageToClient(msg);
            }
        }
    }
    
    public synchronized void addHandler(ClientHandler clientHandler) {
        handlers.add(clientHandler);
    }

    public synchronized void deleteHandler(ClientHandler clientHandler) {
        handlers.remove(clientHandler);
    }
    
    public List<ClientHandler> getHandlers() {
        return handlers;
    }
}
