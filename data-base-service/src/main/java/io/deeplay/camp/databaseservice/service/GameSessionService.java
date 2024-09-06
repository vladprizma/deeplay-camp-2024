package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.dto.DTOMapper;
import io.deeplay.camp.databaseservice.dto.GameSessionDTO;
import io.deeplay.camp.databaseservice.dto.GameSessionRequest;
import io.deeplay.camp.databaseservice.model.GameSession;
import io.deeplay.camp.databaseservice.model.User;
import io.deeplay.camp.databaseservice.repository.GameSessionRepository;
import io.deeplay.camp.databaseservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameSessionService {

    private static final Logger logger = LoggerFactory.getLogger(GameSessionService.class);

    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public GameSessionService(GameSessionRepository gameSessionRepository, UserRepository userRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.userRepository = userRepository;
    }

    public List<GameSessionDTO> getAllSessions() {
        logger.info("Retrieving all game sessions");
        List<GameSessionDTO> sessions = gameSessionRepository.findAll().stream()
                .map(DTOMapper::toGameSessionDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} game sessions", sessions.size());
        return sessions;
    }

    public GameSessionDTO getSessionById(int id) {
        logger.info("Retrieving game session by ID: {}", id);
        Optional<GameSession> sessionOptional = gameSessionRepository.findById(id);
        if (sessionOptional.isPresent()) {
            logger.info("Game session found with ID: {}", id);
            return DTOMapper.toGameSessionDTO(sessionOptional.get());
        } else {
            logger.warn("Game session not found with ID: {}", id);
            return null;
        }
    }

    public GameSessionDTO saveSession(GameSessionRequest gameSessionRequest) {
        logger.info("Saving new game session between player1 ID: {} and player2 ID: {}",
                gameSessionRequest.getPlayer1(), gameSessionRequest.getPlayer2());

        User player1 = userRepository.findById(gameSessionRequest.getPlayer1())
                .orElseThrow(() -> {
                    logger.error("Player1 not found with ID: {}", gameSessionRequest.getPlayer1());
                    return new IllegalArgumentException("Player1 not found with ID: " + gameSessionRequest.getPlayer1());
                });

        User player2 = userRepository.findById(gameSessionRequest.getPlayer2())
                .orElseThrow(() -> {
                    logger.error("Player2 not found with ID: {}", gameSessionRequest.getPlayer2());
                    return new IllegalArgumentException("Player2 not found with ID: " + gameSessionRequest.getPlayer2());
                });

        GameSession newGameSession = new GameSession(
                player1, player2,
                gameSessionRequest.getResult(),
                gameSessionRequest.getLog(),
                gameSessionRequest.getSessionChat()
        );

        GameSession savedGameSession = gameSessionRepository.save(newGameSession);
        logger.info("Saved game session with ID: {}", savedGameSession.getId());

        return DTOMapper.toGameSessionDTO(savedGameSession);
    }

    public void deleteSession(int id) {
        logger.info("Deleting game session with ID: {}", id);

        if (!gameSessionRepository.existsById(id)) {
            logger.warn("Game session not found with ID: {}", id);
            throw new IllegalArgumentException("Game session not found with ID: " + id);
        }

        gameSessionRepository.deleteById(id);
        logger.info("Deleted game session with ID: {}", id);
    }
}
