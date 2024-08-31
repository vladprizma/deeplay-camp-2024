package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.dto.DTOMapper;
import io.deeplay.camp.databaseservice.dto.GameSessionDTO;
import io.deeplay.camp.databaseservice.dto.GameSessionRequest;
import io.deeplay.camp.databaseservice.model.GameSession;
import io.deeplay.camp.databaseservice.repository.GameSessionRepository;
import io.deeplay.camp.databaseservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing game sessions.
 */
@Service
public class GameSessionService {

    /**
     * Repository for accessing game sessions.
     */
    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new GameSessionService with the specified repository.
     *
     * @param gameSessionRepository Repository for accessing game sessions.
     */
    @Autowired
    public GameSessionService(GameSessionRepository gameSessionRepository, UserRepository userRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all game sessions.
     *
     * @return A list of GameSessionDTO objects representing all game sessions.
     */
    public List<GameSessionDTO> getAllSessions() {
        return gameSessionRepository.findAll().stream()
            .map(DTOMapper::toGameSessionDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a game session by its unique identifier.
     *
     * @param id The unique identifier of the game session.
     * @return A GameSessionDTO object representing the game session, or null if not found.
     */
    public GameSessionDTO getSessionById(int id) {
        return DTOMapper.toGameSessionDTO(gameSessionRepository.findById(id).orElse(null));
    }

    /**
     * Saves a new game session.
     *
     * @param gameSession The GameSession object containing the details of the game session to be saved.
     * @return A GameSessionDTO object representing the saved game session.
     */
    public GameSessionDTO saveSession(GameSessionRequest gameSession) {
        var player1 = userRepository.findById(gameSession.getPlayer1()).orElseThrow();
        var player2 = userRepository.findById(gameSession.getPlayer2()).orElseThrow();
        
        var newGameSession = new GameSession(player1, player2, gameSession.getResult(), gameSession.getLog(), gameSession.getSessionChat());
        
        return DTOMapper.toGameSessionDTO(gameSessionRepository.save(newGameSession));
    }

    /**
     * Deletes a game session by its unique identifier.
     *
     * @param id The unique identifier of the game session to be deleted.
     */
    public void deleteSession(int id) {
        gameSessionRepository.deleteById(id);
    }
}