package io.deeplay.camp.databaseservice.controller;

import io.deeplay.camp.databaseservice.dto.TokenDTO;
import io.deeplay.camp.databaseservice.dto.TokenRequest;
import io.deeplay.camp.databaseservice.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

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
        logger.info("Retrieving all tokens");
        List<TokenDTO> tokens = tokenService.getAllTokens();
        logger.info("Retrieved {} tokens", tokens.size());
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
        logger.info("Retrieving token by ID: {}", id);
        TokenDTO token = tokenService.getTokenById(id);
        if (token != null) {
            logger.info("Token found with ID: {}", id);
            return ResponseEntity.ok(token);
        } else {
            logger.warn("Token not found with ID: {}", id);
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
        logger.info("Creating new token");
        TokenDTO savedToken = tokenService.saveToken(token);
        logger.info("Created new token for user: {}", savedToken.getUserId());
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
        logger.info("Deleting token with ID: {}", id);
        if (tokenService.getTokenById(id) != null) {
            tokenService.deleteToken(id);
            logger.info("Deleted token with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Token not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}