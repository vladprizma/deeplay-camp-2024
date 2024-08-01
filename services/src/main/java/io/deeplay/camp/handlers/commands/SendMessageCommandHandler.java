package io.deeplay.camp.handlers.commands;

import entity.ChatMessage;
import io.deeplay.camp.chat.ChatService;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SendMessageCommandHandler implements CommandHandler {

    private final ChatService chatService = new ChatService();

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        String[] parts = message.split(MainHandler.splitRegex, 2);
        String chatMessage = parts[1];

        chatService.addMessage(mainHandler.getUser(), chatMessage);
        mainHandler.sendMessageToClient("Message sent successfully.");

        ChatService chatService = new ChatService();
        StringBuilder response = new StringBuilder("messages");
        
        List<ChatMessage> messages = chatService.getAllMessages();
        for (ChatMessage chatMessage1 : messages) {
            response.append("::").append(chatMessage1.getTimestamp())
                    .append(" ").append(chatMessage1.getUserId().getUsername())
                    .append(" ").append(chatMessage1.getMessage());
        }
        
        SessionManager.getInstance().sendMessageToAll(response.toString());
    }
}