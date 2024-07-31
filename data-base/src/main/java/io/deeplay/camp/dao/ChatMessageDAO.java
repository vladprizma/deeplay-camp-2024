package io.deeplay.camp.dao;

import entity.ChatMessage;
import entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageDAO {
    private static final String DB_URL = "jdbc:postgresql://localhost:5431/reversi";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    public void addMessage(ChatMessage message) throws SQLException {
        String sql = "INSERT INTO chat_messages (user_id, message, timestamp) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, message.getUserId().getId());
            statement.setString(2, message.getMessage());
            statement.setTimestamp(3, message.getTimestamp());
            statement.executeUpdate();
        }
    }

    public List<ChatMessage> getAllMessages() throws SQLException {
        String sql = "SELECT * FROM chat_messages ORDER BY timestamp DESC";
        UserDAO userDAO = new UserDAO();

        List<ChatMessage> messages = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                User user = userDAO.getUserById(userId).get();
                String message = resultSet.getString("message");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");

                messages.add(new ChatMessage(id, user, message, timestamp));
            }
        }

        return messages;
    }

    public void deleteMessage(int id) throws SQLException {
        String sql = "DELETE FROM chat_messages WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
