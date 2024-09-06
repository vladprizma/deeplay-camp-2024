package io.deeplay.camp.databaseservice.controller;

import io.deeplay.camp.databaseservice.dto.GameSessionDTO;
import io.deeplay.camp.databaseservice.dto.GameSessionRequest;
import io.deeplay.camp.databaseservice.service.GameSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing game sessions.
 */
@RestController
@RequestMapping("/api/game-sessions")
public class GameSessionController {

    private static final Logger logger = LoggerFactory.getLogger(GameSessionController.class);

    private final GameSessionService gameSessionService;

    /**
     * Constructs a new GameSessionController with the specified game session service.
     *
     * @param gameSessionService The service for managing game sessions.
     */
    @Autowired
    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    /**
     * Retrieves all game sessions.
     *
     * @return A ResponseEntity containing a list of GameSessionDTO objects.
     */
    @GetMapping
    public ResponseEntity<List<GameSessionDTO>> getAllSessions() {
        logger.info("Retrieving all game sessions");
        List<GameSessionDTO> sessions = gameSessionService.getAllSessions();
        logger.info("Retrieved {} game sessions", sessions.size());
        return ResponseEntity.ok(sessions);
    }

    /**
     * Retrieves a game session by its ID.
     *
     * @param id The ID of the game session to retrieve.
     * @return A ResponseEntity containing the GameSessionDTO object, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameSessionDTO> getSessionById(@PathVariable int id) {
        logger.info("Retrieving game session by ID: {}", id);
        GameSessionDTO session = gameSessionService.getSessionById(id);
        if (session != null) {
            logger.info("Game session found with ID: {}", id);
            return ResponseEntity.ok(session);
        } else {
            logger.warn("Game session not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new game session.
     *
     * @param gameSession The request object containing the details of the game session to create.
     * @return A ResponseEntity containing the saved GameSessionDTO object.
     */
    @PostMapping
    public ResponseEntity<GameSessionDTO> createSession(@RequestBody GameSessionRequest gameSession) {
        logger.info("Creating new game session");
        GameSessionDTO savedSession = gameSessionService.saveSession(gameSession);
        logger.info("Created new game session with ID: {}", savedSession.getId());
        return ResponseEntity.ok(savedSession);
    }

    /**
     * Deletes a game session by its ID.
     *
     * @param id The ID of the game session to delete.
     * @return A ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable int id) {
        logger.info("Deleting game session with ID: {}", id);
        if (gameSessionService.getSessionById(id) != null) {
            gameSessionService.deleteSession(id);
            logger.info("Deleted game session with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Game session not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}