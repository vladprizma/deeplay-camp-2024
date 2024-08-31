package io.deeplay.camp.databaseservice.dto;

import io.deeplay.camp.databaseservice.model.ChatMessage;
import io.deeplay.camp.databaseservice.model.GameSession;
import io.deeplay.camp.databaseservice.model.Token;
import io.deeplay.camp.databaseservice.model.User;

/**
 * Utility class for mapping model objects to Data Transfer Objects (DTOs).
 */
public class DTOMapper {

    /**
     * Converts a ChatMessage model object to a ChatMessageDTO.
     *
     * @param chatMessage The ChatMessage model object to convert.
     * @return The corresponding ChatMessageDTO.
     */
    public static ChatMessageDTO toChatMessageDTO(ChatMessage chatMessage) {
        return new ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getUser(),
                chatMessage.getMessage(),
                chatMessage.getTimestamp()
        );
    }

    /**
     * Converts a GameSession model object to a GameSessionDTO.
     *
     * @param gameSession The GameSession model object to convert.
     * @return The corresponding GameSessionDTO.
     */
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

    /**
     * Converts a Token model object to a TokenDTO.
     *
     * @param token The Token model object to convert.
     * @return The corresponding TokenDTO.
     */
    public static TokenDTO toTokenDTO(Token token) {
        return new TokenDTO(
                token.getId(),
                token.getRefreshToken(),
                token.getUpdateToken()
        );
    }

    /**
     * Converts a User model object to a UserDTO.
     *
     * @param user The User model object to convert.
     * @return The corresponding UserDTO.
     */
    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getUserPhoto(),
                user.getRating(),
                user.getMatches(),
                user.getPassword()
        );
    }
}