package io.deeplay.camp.databaseservice.controller;

import io.deeplay.camp.databaseservice.dto.ChatMessageDTO;
import io.deeplay.camp.databaseservice.dto.ChatMessageRequest;
import io.deeplay.camp.databaseservice.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing chat messages.
 */
@RestController
@RequestMapping("/api/chat-messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * Constructs a new ChatMessageController with the specified chat message service.
     *
     * @param chatMessageService The service for managing chat messages.
     */
    @Autowired
    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    /**
     * Retrieves all chat messages.
     *
     * @return A ResponseEntity containing a list of ChatMessageDTO objects.
     */
    @GetMapping
    public ResponseEntity<List<ChatMessageDTO>> getAllMessages() {
        List<ChatMessageDTO> messages = chatMessageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    /**
     * Retrieves a chat message by its ID.
     *
     * @param id The ID of the chat message to retrieve.
     * @return A ResponseEntity containing the ChatMessageDTO object, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChatMessageDTO> getMessageById(@PathVariable int id) {
        ChatMessageDTO message = chatMessageService.getMessageById(id);
        if (message != null) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new chat message.
     *
     * @param chatMessage The request object containing the details of the chat message to create.
     * @return A ResponseEntity containing the saved ChatMessageDTO object.
     */
    @PostMapping
    public ResponseEntity<ChatMessageDTO> createMessage(@RequestBody ChatMessageRequest chatMessage) {
        ChatMessageDTO savedMessage = chatMessageService.saveMessage(chatMessage);
        return ResponseEntity.ok(savedMessage);
    }

    /**
     * Deletes a chat message by its ID.
     *
     * @param id The ID of the chat message to delete.
     * @return A ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int id) {
        chatMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}