package io.deeplay.camp.databaseservice.controller;

import io.deeplay.camp.databaseservice.dto.GameSessionDTO;
import io.deeplay.camp.databaseservice.model.GameSession;
import io.deeplay.camp.databaseservice.service.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game-sessions")
public class GameSessionController {

    private final GameSessionService gameSessionService;

    @Autowired
    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @GetMapping
    public ResponseEntity<List<GameSessionDTO>> getAllSessions() {
        List<GameSessionDTO> sessions = gameSessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameSessionDTO> getSessionById(@PathVariable int id) {
        GameSessionDTO session = gameSessionService.getSessionById(id);
        if (session != null) {
            return ResponseEntity.ok(session);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GameSessionDTO> createSession(@RequestBody GameSession gameSession) {
        GameSessionDTO savedSession = gameSessionService.saveSession(gameSession);
        return ResponseEntity.ok(savedSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable int id) {
        gameSessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
