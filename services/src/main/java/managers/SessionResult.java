package managers;

import entity.GameSession;
import entity.User;

public class SessionResult {
    private GameSession gameSession;
    private User player;

    public SessionResult(GameSession session, User player) {
        gameSession = session;
        this.player = player;
    }
    
    public GameSession getGameSession() {
        return gameSession;
    }
    
    public User getPlayer() {
        return player;
    }
}
