package io.deeplay.camp.dao;

import entity.User;
import entity.User;

import java.sql.*;
import java.util.Optional;

public class UserDAO {
    private static final String DB_URL = "jdbc:postgresql://localhost:5431/reversi";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    public int addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, \"userPhoto\", rating, matches) VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUserPhoto());
            statement.setInt(4, user.getRating());
            statement.setInt(5, user.getMatches());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve the generated ID.");
            }
        }
    }

    public void deleteUserById(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }

    public void deleteUserByUsername(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET password = ?, userPhoto = ?, rating = ?, matches = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getUserPhoto());
            statement.setInt(3, user.getRating());
            statement.setInt(4, user.getMatches());
            statement.setInt(5, user.getId());
            statement.executeUpdate();
        }
    }

    public Optional<User> getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapRowToPlayer(resultSet));
            }
        }

        return Optional.empty();
    }

    public Optional<User> getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapRowToPlayer(resultSet));
            }
        }

        return Optional.empty();
    }

    private User mapRowToPlayer(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getInt("rating"),
                resultSet.getInt("matches"),
                resultSet.getString("userPhoto")
        );
    }
}