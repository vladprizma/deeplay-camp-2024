package io.deeplay.camp.databaseservice.controller;

import io.deeplay.camp.databaseservice.dto.TokenDTO;
import io.deeplay.camp.databaseservice.dto.TokenRequest;
import io.deeplay.camp.databaseservice.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing tokens.
 */
@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    private final TokenService tokenService;

    /**
     * Constructs a new TokenController with the specified token service.
     *
     * @param tokenService The service for managing tokens.
     */
    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Retrieves all tokens.
     *
     * @return A ResponseEntity containing a list of TokenDTO objects.
     */
    @GetMapping
    public ResponseEntity<List<TokenDTO>> getAllTokens() {
        List<TokenDTO> tokens = tokenService.getAllTokens();
        return ResponseEntity.ok(tokens);
    }

    /**
     * Retrieves a token by its ID.
     *
     * @param id The ID of the token to retrieve.
     * @return A ResponseEntity containing the TokenDTO object, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TokenDTO> getTokenById(@PathVariable int id) {
        TokenDTO token = tokenService.getTokenById(id);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new token.
     *
     * @param token The request object containing the details of the token to create.
     * @return A ResponseEntity containing the saved TokenDTO object.
     */
    @PostMapping
    public ResponseEntity<TokenDTO> createToken(@RequestBody TokenRequest token) {
        TokenDTO savedToken = tokenService.saveToken(token);
        return ResponseEntity.ok(savedToken);
    }

    /**
     * Deletes a token by its ID.
     *
     * @param id The ID of the token to delete.
     * @return A ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToken(@PathVariable int id) {
        tokenService.deleteToken(id);
        return ResponseEntity.noContent().build();
    }
}