package io.deeplay.camp.dao;

import entity.GameSession;
import entity.SessionMessage;
import entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing game sessions in the database.
 * <p>
 * This class provides methods to add, update, retrieve, and delete game sessions from the database.
 * </p>
 */
public class GameSessionDAO {
    private static final UserDAO USER_DAO = new UserDAO();
    private static final String DB_URL = "jdbc:postgresql://localhost:5431/reversi";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    /**
     * Adds a new game session to the database.
     *
     * @param gameSession The game session to be added.
     * @param log         The game log.
     * @throws SQLException If a database access error occurs.
     */
    public void addGameSession(GameSession gameSession, String log) throws SQLException {
        String sql = "INSERT INTO gamesessions (player1_id, result, log, player2_id, session_chat) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gameSession.getPlayer1().getId());
            statement.setString(2, gameSession.getResult());
            statement.setString(3, log);
            statement.setInt(4, gameSession.getPlayer2().getId());
            statement.setString(5, convertChatToString(gameSession.getSessionChat()));
            statement.executeUpdate();
        }
    }

    /**
     * Updates an existing game session in the database.
     *
     * @param gameSession The game session to be updated.
     * @param log         The game log.
     * @throws SQLException If a database access error occurs.
     */
    public void updateGameSession(GameSession gameSession, String log) throws SQLException {
        String sql = "UPDATE gamesessions SET player1_id = ?, result = ?, log = ?, player2_id = ?, session_chat = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gameSession.getPlayer1().getId());
            statement.setString(2, gameSession.getResult());
            statement.setString(3, log);
            statement.setInt(4, gameSession.getPlayer2().getId());
            statement.setString(5, convertChatToString(gameSession.getSessionChat()));
            statement.setInt(6, gameSession.getSessionId());
            statement.executeUpdate();
        }
    }

    /**
     * Retrieves a game session from the database by its ID.
     *
     * @param id The ID of the game session to be retrieved.
     * @return The game session with the specified ID, or null if not found.
     * @throws SQLException If a database access error occurs.
     */
    public GameSession getGameSessionById(int id) throws SQLException {
        String sql = "SELECT * FROM gamesessions WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int player1Id = resultSet.getInt("player1_id");
                    String result = resultSet.getString("result");
                    String log = resultSet.getString("log");
                    int player2Id = resultSet.getInt("player2_id");
                    String sessionChat = resultSet.getString("session_chat");

                    var player1 = USER_DAO.getUserById(player1Id);
                    var player2 = USER_DAO.getUserById(player2Id);

                    GameSession gameSession = new GameSession(id, player1.get(), result, player2.get(), log);
                    gameSession.setSessionChat(convertStringToChat(sessionChat));

                    return gameSession;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all game sessions from the database.
     *
     * @return A list of all game sessions.
     * @throws SQLException If a database access error occurs.
     */
    public List<GameSession> getAllGameSessions() throws SQLException {
        String sql = "SELECT * FROM gamesessions";

        List<GameSession> gameSessions = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int player1Id = resultSet.getInt("player1_id");
                String result = resultSet.getString("result");
                int player2Id = resultSet.getInt("player2_id");
                String log = resultSet.getString("log");
                String sessionChat = resultSet.getString("session_chat");

                var player1 = USER_DAO.getUserById(player1Id);
                var player2 = USER_DAO.getUserById(player2Id);

                GameSession gameSession = new GameSession(id, player1.get(), result, player2.get(), log);
                gameSession.setSessionChat(convertStringToChat(sessionChat));

                gameSessions.add(gameSession);
            }
        }
        return gameSessions;
    }

    /**
     * Deletes a game session from the database by its ID.
     *
     * @param id The ID of the game session to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteGameSession(int id) throws SQLException {
        String sql = "DELETE FROM gamesessions WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    /**
     * Converts a list of session messages to a string representation.
     *
     * @param sessionChat The list of session messages.
     * @return The string representation of the session messages.
     */
    private String convertChatToString(List<SessionMessage> sessionChat) {
        StringBuilder sb = new StringBuilder();
        for (SessionMessage message : sessionChat) {
            sb.append(message.getUsername()).append("::").append(message.getMsg()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Converts a string representation of session messages to a list.
     *
     * @param chat The string representation of the session messages.
     * @return The list of session messages.
     */
    private List<SessionMessage> convertStringToChat(String chat) {
        List<SessionMessage> sessionChat = new ArrayList<>();
        if (chat != null && !chat.isEmpty()) {
            String[] messages = chat.split("\n");
            for (String message : messages) {
                String[] parts = message.split("::");
                if (parts.length == 2) {
                    sessionChat.add(new SessionMessage(parts[1], parts[0]));
                }
            }
        }
        return sessionChat;
    }
}