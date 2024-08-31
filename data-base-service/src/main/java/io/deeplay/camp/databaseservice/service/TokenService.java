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

@Service
public class TokenService {

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
        return tokenRepository.findAll().stream()
            .map(DTOMapper::toTokenDTO)
            .collect(Collectors.toList());
    }

    public TokenDTO getTokenById(int id) {
        return DTOMapper.toTokenDTO(tokenRepository.findById(id).orElse(null));
    }

    @Transactional
    public TokenDTO saveToken(TokenRequest token) {
//        tokenRepository.deleteByUser(userRepository.findById(token.getUserId()).get());
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

    public void deleteToken(int id) {
        tokenRepository.deleteById(id);
    }
}
