package io.deeplay.camp.databaseservice.controller;

import io.deeplay.camp.databaseservice.dto.GameSessionDTO;
import io.deeplay.camp.databaseservice.dto.GameSessionRequest;
import io.deeplay.camp.databaseservice.model.GameSession;
import io.deeplay.camp.databaseservice.service.GameSessionService;
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
        List<GameSessionDTO> sessions = gameSessionService.getAllSessions();
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
        GameSessionDTO session = gameSessionService.getSessionById(id);
        if (session != null) {
            return ResponseEntity.ok(session);
        } else {
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
        GameSessionDTO savedSession = gameSessionService.saveSession(gameSession);
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
        gameSessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}