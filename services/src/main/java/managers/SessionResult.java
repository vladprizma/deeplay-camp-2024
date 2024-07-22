package managers;

import entity.GameSession;
import entity.Player;

public class SessionResult {
    public GameSession gameSession;
    public Player player;

    public SessionResult(GameSession session, Player player) {
        gameSession = session;
        this.player = player;
    }
}
