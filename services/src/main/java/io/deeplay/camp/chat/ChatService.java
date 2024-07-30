package io.deeplay.camp.chat;

import entity.ChatMessage;
import io.deeplay.camp.dao.ChatMessageDAO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ChatService {
    private final ChatMessageDAO chatMessageDAO = new ChatMessageDAO();

    public void addMessage(int userId, String message) throws SQLException {
        ChatMessage chatMessage = new ChatMessage(0, userId, message, new Timestamp(System.currentTimeMillis()));
        chatMessageDAO.addMessage(chatMessage);
    }

    public List<ChatMessage> getAllMessages() throws SQLException {
        return chatMessageDAO.getAllMessages();
    }

    public void deleteMessage(int id) throws SQLException {
        chatMessageDAO.deleteMessage(id);
    }
}
