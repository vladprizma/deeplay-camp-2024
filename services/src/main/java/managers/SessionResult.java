package managers;

import entity.GameSession;
import entity.Player;

public class SessionResult {
    private GameSession gameSession;
    private Player player;

    public SessionResult(GameSession session, Player player) {
        gameSession = session;
        this.player = player;
    }
    
    public GameSession getGameSession() {
        return gameSession;
    }
    
    public Player getPlayer() {
        return player;
    }
}
