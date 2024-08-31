package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.dto.ChatMessageDTO;
import io.deeplay.camp.databaseservice.dto.ChatMessageRequest;
import io.deeplay.camp.databaseservice.dto.DTOMapper;
import io.deeplay.camp.databaseservice.model.ChatMessage;
import io.deeplay.camp.databaseservice.repository.ChatMessageRepository;
import io.deeplay.camp.databaseservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing chat messages.
 */
@Service
public class ChatMessageService {

    /**
     * Repository for accessing chat messages.
     */
    private final ChatMessageRepository chatMessageRepository;

    /**
     * Repository for accessing users.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a new ChatMessageService with the specified repositories.
     *
     * @param chatMessageRepository Repository for accessing chat messages.
     * @param userRepository Repository for accessing users.
     */
    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all chat messages.
     *
     * @return A list of ChatMessageDTO objects representing all chat messages.
     */
    public List<ChatMessageDTO> getAllMessages() {
        return chatMessageRepository.findAll().stream()
                .map(DTOMapper::toChatMessageDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a chat message by its unique identifier.
     *
     * @param id The unique identifier of the chat message.
     * @return A ChatMessageDTO object representing the chat message, or null if not found.
     */
    public ChatMessageDTO getMessageById(int id) {
        return DTOMapper.toChatMessageDTO(chatMessageRepository.findById(id).orElse(null));
    }

    /**
     * Saves a new chat message.
     *
     * @param chatMessage The ChatMessageRequest object containing the details of the chat message to be saved.
     * @return A ChatMessageDTO object representing the saved chat message.
     */
    public ChatMessageDTO saveMessage(ChatMessageRequest chatMessage) {
        var newChatMessage = new ChatMessage(
                userRepository.findById(chatMessage.getUserId()).orElse(null),
                chatMessage.getMessage(),
                chatMessage.getTimestamp()
        );

        chatMessageRepository.save(newChatMessage);

        return DTOMapper.toChatMessageDTO(newChatMessage);
    }

    /**
     * Deletes a chat message by its unique identifier.
     *
     * @param id The unique identifier of the chat message to be deleted.
     */
    public void deleteMessage(int id) {
        chatMessageRepository.deleteById(id);
    }
}