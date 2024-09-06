package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.dto.ChatMessageDTO;
import io.deeplay.camp.databaseservice.dto.ChatMessageRequest;
import io.deeplay.camp.databaseservice.dto.DTOMapper;
import io.deeplay.camp.databaseservice.model.ChatMessage;
import io.deeplay.camp.databaseservice.model.User;
import io.deeplay.camp.databaseservice.repository.ChatMessageRepository;
import io.deeplay.camp.databaseservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    public List<ChatMessageDTO> getAllMessages() {
        logger.info("Retrieving all chat messages");
        List<ChatMessageDTO> messages = chatMessageRepository.findAll().stream()
                .map(DTOMapper::toChatMessageDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} chat messages", messages.size());
        return messages;
    }

    public ChatMessageDTO getMessageById(int id) {
        logger.info("Retrieving chat message by ID: {}", id);
        Optional<ChatMessage> chatMessageOptional = chatMessageRepository.findById(id);
        if (chatMessageOptional.isPresent()) {
            logger.info("Chat message found with ID: {}", id);
            return DTOMapper.toChatMessageDTO(chatMessageOptional.get());
        } else {
            logger.warn("Chat message not found with ID: {}", id);
            return null;
        }
    }

    public ChatMessageDTO saveMessage(ChatMessageRequest chatMessageRequest) {
        logger.info("Saving new chat message from user ID: {}", chatMessageRequest.getUserId());

        User user = userRepository.findById(chatMessageRequest.getUserId()).orElse(null);
        if (user == null) {
            logger.error("User not found with ID: {}", chatMessageRequest.getUserId());
            throw new IllegalArgumentException("User not found with ID: " + chatMessageRequest.getUserId());
        }

        ChatMessage newChatMessage = new ChatMessage(
                user,
                chatMessageRequest.getMessage(),
                chatMessageRequest.getTimestamp()
        );

        ChatMessage savedChatMessage = chatMessageRepository.save(newChatMessage);
        logger.info("Saved chat message with ID: {}", savedChatMessage.getId());

        return DTOMapper.toChatMessageDTO(savedChatMessage);
    }

    public void deleteMessage(int id) {
        logger.info("Deleting chat message with ID: {}", id);

        if (!chatMessageRepository.existsById(id)) {
            logger.warn("Chat message not found with ID: {}", id);
            throw new IllegalArgumentException("Chat message not found with ID: " + id);
        }

        chatMessageRepository.deleteById(id);
        logger.info("Deleted chat message with ID: {}", id);
    }
}