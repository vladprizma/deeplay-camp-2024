package io.deeplay.camp.gameSession;

import io.deeplay.camp.entity.GameSession;
import io.deeplay.camp.dao.GameSessionDAO;
import java.sql.SQLException;
import java.util.List;

public class GameSessionService {
    private final GameSessionDAO gameSessionDAO;

    public GameSessionService() {
        this.gameSessionDAO = new GameSessionDAO();
    }

    public void addGameSession(GameSession gameSession, List<String> log) throws SQLException {
        var stringLog = convertLogToString(log);
        
        gameSessionDAO.addGameSession(gameSession, stringLog);
    }

    public void updateGameSession(GameSession gameSession, String log) throws SQLException {
        gameSessionDAO.updateGameSession(gameSession, log);
    }

    public GameSession getGameSessionById(int id) throws SQLException {
        return gameSessionDAO.getGameSessionById(id);
    }

    public List<GameSession> getAllGameSessions() throws SQLException {
        return gameSessionDAO.getAllGameSessions();
    }

    public void deleteGameSession(int id) throws SQLException {
        gameSessionDAO.deleteGameSession(id);
    }
    
    private String convertLogToString(List<String> log) {
        return String.join("\n", log);
    }
}
