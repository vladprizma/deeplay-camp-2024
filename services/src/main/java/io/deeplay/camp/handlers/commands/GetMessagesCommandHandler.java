package io.deeplay.camp.handlers.commands;

import entity.ChatMessage;
import io.deeplay.camp.chat.ChatService;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetMessagesCommandHandler implements CommandHandler {

    private final ChatService chatService = new ChatService();

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        List<ChatMessage> messages = chatService.getAllMessages();
        StringBuilder response = new StringBuilder("Chat messages:");
        for (ChatMessage chatMessage : messages) {
            response.append("\n").append(chatMessage.getTimestamp())
                    .append(" - ").append(chatMessage.getUserId())
                    .append(": ").append(chatMessage.getMessage());
        }
        mainHandler.sendMessageToClient(response.toString());
    }
}
