package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.model.ChatMessage;
import io.deeplay.camp.databaseservice.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }

    public ChatMessage getMessageById(int id) {
        return chatMessageRepository.findById(id).orElse(null);
    }

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public void deleteMessage(int id) {
        chatMessageRepository.deleteById(id);
    }
}
