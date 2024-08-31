package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.dto.DTOMapper;
import io.deeplay.camp.databaseservice.dto.TokenDTO;
import io.deeplay.camp.databaseservice.dto.TokenRequest;
import io.deeplay.camp.databaseservice.model.Token;
import io.deeplay.camp.databaseservice.model.User;
import io.deeplay.camp.databaseservice.repository.TokenRepository;
import io.deeplay.camp.databaseservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository, UserService userService, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public List<TokenDTO> getAllTokens() {
        logger.info("Retrieving all tokens");
        List<TokenDTO> tokens = tokenRepository.findAll().stream()
                .map(DTOMapper::toTokenDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} tokens", tokens.size());
        return tokens;
    }

    public TokenDTO getTokenById(int id) {
        logger.info("Retrieving token by ID: {}", id);
        Optional<Token> tokenOptional = tokenRepository.findById(id);
        if (tokenOptional.isPresent()) {
            logger.info("Token found with ID: {}", id);
            return DTOMapper.toTokenDTO(tokenOptional.get());
        } else {
            logger.warn("Token not found with ID: {}", id);
            return null;
        }
    }

    @Transactional
    public TokenDTO saveToken(TokenRequest tokenRequest) {
        logger.info("Saving token for user ID: {}", tokenRequest.getUserId());

        User user = userRepository.findById(tokenRequest.getUserId())
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", tokenRequest.getUserId());
                    return new IllegalArgumentException("User not found with ID: " + tokenRequest.getUserId());
                });

        Token token = tokenRepository.findByUserId(tokenRequest.getUserId());

        if (token != null) {
            logger.info("Updating existing token for user ID: {}", tokenRequest.getUserId());
            token.setRefreshToken(tokenRequest.getRefreshToken());
            token.setUpdateToken(tokenRequest.getUpdateToken());
        } else {
            logger.info("Creating new token for user ID: {}", tokenRequest.getUserId());
            token = new Token();
            token.setRefreshToken(tokenRequest.getRefreshToken());
            token.setUpdateToken(tokenRequest.getUpdateToken());
            token.setUser(user);
        }

        Token savedToken = tokenRepository.save(token);
        logger.info("Saved token with ID: {}", savedToken.getId());

        return DTOMapper.toTokenDTO(savedToken);
    }

    public void deleteToken(int id) {
        logger.info("Deleting token with ID: {}", id);

        if (!tokenRepository.existsById(id)) {
            logger.warn("Token not found with ID: {}", id);
            throw new IllegalArgumentException("Token not found with ID: " + id);
        }

        tokenRepository.deleteById(id);
        logger.info("Deleted token with ID: {}", id);
    }
}
