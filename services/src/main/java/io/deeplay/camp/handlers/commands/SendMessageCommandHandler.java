package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.chat.ChatService;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;

public class SendMessageCommandHandler implements CommandHandler {

    private final ChatService chatService = new ChatService();

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        String[] parts = message.split(MainHandler.splitRegex, 2);
        String chatMessage = parts[1];

        chatService.addMessage(mainHandler.getUser(), chatMessage);
        mainHandler.sendMessageToClient("Message sent successfully.");
    }
}