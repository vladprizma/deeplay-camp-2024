package session;

import entity.GameSession;
import entity.Player;
import enums.Color;
import enums.GameStatus;
import handlers.ClientHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class SessionManager {
    private static SessionManager instance;
    private final List<GameSession> sessions;
    
    private SessionManager() {
        this.sessions = new ArrayList<>();
    }
    
    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }
    
    public synchronized GameSession findOrCreateSession(ClientHandler clientHandler) {
        for (GameSession session : sessions) {
            if (session.players.size() < 2 && session.gameState == GameStatus.NOT_STARTED) {
                session.players.put("1", new Player("1", Color.WHITE));
                session.gameState = GameStatus.IN_PROGRESS;
                return session;
            }
        }
        GameSession newSession = new GameSession();
        newSession.players.put("1", new Player("1", Color.WHITE));
        sessions.add(newSession);
        return newSession;
    }
}
