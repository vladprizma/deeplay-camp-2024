package io.deeplay.camp.databaseservice.dto;

import io.deeplay.camp.databaseservice.model.ChatMessage;
import io.deeplay.camp.databaseservice.model.GameSession;
import io.deeplay.camp.databaseservice.model.Token;
import io.deeplay.camp.databaseservice.model.User;

public class DTOMapper {

    public static ChatMessageDTO toChatMessageDTO(ChatMessage chatMessage) {
        return new ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getUser().getId(),
                chatMessage.getMessage(),
                chatMessage.getTimestamp()
        );
    }

    public static GameSessionDTO toGameSessionDTO(GameSession gameSession) {
        return new GameSessionDTO(
                gameSession.getId(),
                gameSession.getPlayer1().getId(),
                gameSession.getPlayer2().getId(),
                gameSession.getResult(),
                gameSession.getLog(),
                gameSession.getSessionChat()
        );
    }

    public static TokenDTO toTokenDTO(Token token) {
        return new TokenDTO(
                token.getId(),
                token.getUser().getId(),
                token.getRefreshToken(),
                token.getUpdateToken()
        );
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getUserPhoto(),
                user.getRating(),
                user.getMatches()
        );
    }
}

