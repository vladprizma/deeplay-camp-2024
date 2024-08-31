package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.dto.DTOMapper;
import io.deeplay.camp.databaseservice.dto.GameSessionDTO;
import io.deeplay.camp.databaseservice.model.GameSession;
import io.deeplay.camp.databaseservice.repository.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;

    @Autowired
    public GameSessionService(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
    }

    public List<GameSessionDTO> getAllSessions() {
        return gameSessionRepository.findAll().stream()
            .map(DTOMapper::toGameSessionDTO)
            .collect(Collectors.toList());
    }

    public GameSessionDTO getSessionById(int id) {
        return DTOMapper.toGameSessionDTO(gameSessionRepository.findById(id).orElse(null));
    }

    public GameSessionDTO saveSession(GameSession gameSession) {
        return DTOMapper.toGameSessionDTO(gameSessionRepository.save(gameSession));
    }

    public void deleteSession(int id) {
        gameSessionRepository.deleteById(id);
    }
}
