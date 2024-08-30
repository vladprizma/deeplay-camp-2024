package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.model.GameSession;
import io.deeplay.camp.databaseservice.repository.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;

    @Autowired
    public GameSessionService(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
    }

    public List<GameSession> getAllSessions() {
        return gameSessionRepository.findAll();
    }

    public GameSession getSessionById(int id) {
        return gameSessionRepository.findById(id).orElse(null);
    }

    public GameSession saveSession(GameSession gameSession) {
        return gameSessionRepository.save(gameSession);
    }

    public void deleteSession(int id) {
        gameSessionRepository.deleteById(id);
    }
}
