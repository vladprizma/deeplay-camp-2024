package io.deeplay.camp.dao;

import java.sql.*;
import java.util.Optional;

public class TokenDAO {
    private static final String DB_URL = "jdbc:postgresql://localhost:5431/reversi";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    public void saveToken(int userId, String refreshToken, String updateToken) throws SQLException {
        String sql = "INSERT INTO tokens (user_id, refresh_token, update_token) VALUES (?, ?, ?) " +
                "ON CONFLICT (user_id) DO UPDATE SET refresh_token = EXCLUDED.refresh_token, update_token = EXCLUDED.update_token";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, refreshToken);
            statement.setString(3, updateToken);
            statement.executeUpdate();
        }
    }

    public Optional<String> getRefreshToken(int userId) throws SQLException {
        String sql = "SELECT refresh_token FROM tokens WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getString("refresh_token"));
            }
        }

        return Optional.empty();
    }

    public Optional<String> getUpdateToken(int userId) throws SQLException {
        String sql = "SELECT update_token FROM tokens WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getString("update_token"));
            }
        }

        return Optional.empty();
    }

    public void deleteTokens(int userId) throws SQLException {
        String sql = "DELETE FROM tokens WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }
}
