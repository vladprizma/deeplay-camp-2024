package io.deeplay.camp.dao;

import io.deeplay.camp.entity.User;

import java.sql.*;
import java.util.Optional;

/**
 * Data Access Object (DAO) for managing users in the database.
 * <p>
 * This class provides methods to add, update, retrieve, and delete users from the database.
 * </p>
 */
public class UserDAO {
    private static final String DB_URL = "jdbc:postgresql://localhost:5431/reversi";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";
    
    /**
     * Adds a new user to the database.
     *
     * @param user The user to be added.
     * @return The ID of the newly added user.
     * @throws SQLException If a database access error occurs.
     */
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

    /**
     * Deletes a user from the database by their ID.
     *
     * @param userId The ID of the user to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteUserById(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }

    /**
     * Deletes a user from the database by their username.
     *
     * @param username The username of the user to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteUserByUsername(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user The user to be updated.
     * @throws SQLException If a database access error occurs.
     */
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET password = ?, \"userPhoto\" = ?, rating = ?, matches = ? WHERE id = ?";

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

    /**
     * Retrieves a user from the database by their ID.
     *
     * @param userId The ID of the user to be retrieved.
     * @return An Optional containing the user if found, or an empty Optional if not found.
     * @throws SQLException If a database access error occurs.
     */
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

    /**
     * Retrieves a user from the database by their username.
     *
     * @param username The username of the user to be retrieved.
     * @return An Optional containing the user if found, or an empty Optional if not found.
     * @throws SQLException If a database access error occurs.
     */
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

    /**
     * Maps a row from the result set to a User object.
     *
     * @param resultSet The result set containing the user data.
     * @return The User object.
     * @throws SQLException If a database access error occurs.
     */
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