package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.model.Token;
import io.deeplay.camp.databaseservice.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public List<Token> getAllTokens() {
        return tokenRepository.findAll();
    }

    public Token getTokenById(int id) {
        return tokenRepository.findById(id).orElse(null);
    }

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public void deleteToken(int id) {
        tokenRepository.deleteById(id);
    }
}
