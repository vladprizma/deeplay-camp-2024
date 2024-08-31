package io.deeplay.camp.databaseservice.controller;

import io.deeplay.camp.databaseservice.dto.ChatMessageDTO;
import io.deeplay.camp.databaseservice.dto.ChatMessageRequest;
import io.deeplay.camp.databaseservice.model.ChatMessage;
import io.deeplay.camp.databaseservice.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @GetMapping
    public ResponseEntity<List<ChatMessageDTO>> getAllMessages() {
        List<ChatMessageDTO> messages = chatMessageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatMessageDTO> getMessageById(@PathVariable int id) {
        ChatMessageDTO message = chatMessageService.getMessageById(id);
        if (message != null) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ChatMessageDTO> createMessage(@RequestBody ChatMessageRequest chatMessage) {
        ChatMessageDTO savedMessage = chatMessageService.saveMessage(chatMessage);
        return ResponseEntity.ok(savedMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int id) {
        chatMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
