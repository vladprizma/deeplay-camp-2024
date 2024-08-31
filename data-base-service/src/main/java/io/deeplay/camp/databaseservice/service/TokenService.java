package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.dto.DTOMapper;
import io.deeplay.camp.databaseservice.dto.TokenDTO;
import io.deeplay.camp.databaseservice.dto.TokenRequest;
import io.deeplay.camp.databaseservice.model.Token;
import io.deeplay.camp.databaseservice.repository.TokenRepository;
import io.deeplay.camp.databaseservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing tokens.
 */
@Service
public class TokenService {

    /**
     * Repository for accessing tokens.
     */
    private final TokenRepository tokenRepository;

    /**
     * Service for managing users.
     */
    private final UserService userService;

    /**
     * Repository for accessing users.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a new TokenService with the specified repositories and services.
     *
     * @param tokenRepository Repository for accessing tokens.
     * @param userService Service for managing users.
     * @param userRepository Repository for accessing users.
     */
    @Autowired
    public TokenService(TokenRepository tokenRepository, UserService userService, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all tokens.
     *
     * @return A list of TokenDTO objects representing all tokens.
     */
    public List<TokenDTO> getAllTokens() {
        return tokenRepository.findAll().stream()
            .map(DTOMapper::toTokenDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a token by its unique identifier.
     *
     * @param id The unique identifier of the token.
     * @return A TokenDTO object representing the token, or null if not found.
     */
    public TokenDTO getTokenById(int id) {
        return DTOMapper.toTokenDTO(tokenRepository.findById(id).orElse(null));
    }

    /**
     * Saves a new token or updates an existing one.
     *
     * @param token The TokenRequest object containing the details of the token to be saved.
     * @return A TokenDTO object representing the saved token.
     */
    @Transactional
    public TokenDTO saveToken(TokenRequest token) {
        var changedTokens = tokenRepository.findByUserId(token.getUserId());

        if (changedTokens != null) {
            changedTokens.setRefreshToken(token.getRefreshToken());
            changedTokens.setUpdateToken(token.getUpdateToken());
        } else {
            changedTokens = new Token();
            changedTokens.setRefreshToken(token.getRefreshToken());
            changedTokens.setUpdateToken(token.getUpdateToken());
            changedTokens.setUser(userRepository.findById(token.getUserId()).get());
        }

        Token tokenSaved = tokenRepository.save(changedTokens);

        return DTOMapper.toTokenDTO(tokenSaved);
    }

    /**
     * Deletes a token by its unique identifier.
     *
     * @param id The unique identifier of the token to be deleted.
     */
    public void deleteToken(int id) {
        tokenRepository.deleteById(id);
    }
}