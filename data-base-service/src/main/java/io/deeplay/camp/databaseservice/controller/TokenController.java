package io.deeplay.camp.databaseservice.controller;

import io.deeplay.camp.databaseservice.model.Token;
import io.deeplay.camp.databaseservice.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<List<Token>> getAllTokens() {
        List<Token> tokens = tokenService.getAllTokens();
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Token> getTokenById(@PathVariable int id) {
        Token token = tokenService.getTokenById(id);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Token> createToken(@RequestBody Token token) {
        Token savedToken = tokenService.saveToken(token);
        return ResponseEntity.ok(savedToken);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToken(@PathVariable int id) {
        tokenService.deleteToken(id);
        return ResponseEntity.noContent().build();
    }
}
