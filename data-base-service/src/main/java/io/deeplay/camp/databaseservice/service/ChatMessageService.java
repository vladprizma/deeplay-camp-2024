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

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    public List<ChatMessageDTO> getAllMessages() {
        return chatMessageRepository.findAll().stream()
                .map(DTOMapper::toChatMessageDTO)
                .collect(Collectors.toList());
    }

    public ChatMessageDTO getMessageById(int id) {
        return DTOMapper.toChatMessageDTO(chatMessageRepository.findById(id).orElse(null));
    }

    public ChatMessageDTO saveMessage(ChatMessageRequest chatMessage) {
        var newChatMessage = new ChatMessage(
                userRepository.findById(chatMessage.getUserId()).orElse(null),
                chatMessage.getMessage(),
                chatMessage.getTimestamp()
        );
        
        chatMessageRepository.save(newChatMessage);
        
        return DTOMapper.toChatMessageDTO(newChatMessage);
    }

    public void deleteMessage(int id) {
        chatMessageRepository.deleteById(id);
    }
}
