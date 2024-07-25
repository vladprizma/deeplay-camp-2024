package io.deeplay.camp.dao;

import entity.GameSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameSessionDAO {
    private static final UserDAO USER_DAO = new UserDAO();
    private static final String DB_URL = "jdbc:postgresql://localhost:5431/reversi";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    public void addGameSession(GameSession gameSession, String log) throws SQLException {
        String sql = "INSERT INTO gamesessions (player1_id, result, log, player2_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gameSession.getPlayer1().getId());
            statement.setString(2, gameSession.getResult());
            statement.setString(3, log);
            statement.setInt(4, gameSession.getPlayer2().getId());
            statement.executeUpdate();
        }
    }

    public void updateGameSession(GameSession gameSession, String log) throws SQLException {
        String sql = "UPDATE gamesessions SET player1_id = ?, result = ?, log = ?, player2_id = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gameSession.getPlayer1().getId());
            statement.setString(2, gameSession.getResult());
            statement.setString(3, log);
            statement.setInt(4, gameSession.getPlayer2().getId());
            statement.setInt(5, gameSession.getSessionId());
            statement.executeUpdate();
        }
    }

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
                    
                    var player1 = USER_DAO.getUserById(player1Id);
                    var player2 = USER_DAO.getUserById(player2Id);
                    
                    return new GameSession(id, player1.get(), result, player2.get(), log);
                }
            }
        }
        return null;
    }

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

                var player1 = USER_DAO.getUserById(player1Id);
                var player2 = USER_DAO.getUserById(player2Id);
                
                gameSessions.add(new GameSession(id, player1.get(), result, player2.get(), log));
            }
        }
        return gameSessions;
    }

    public void deleteGameSession(int id) throws SQLException {
        String sql = "DELETE FROM gamesessions WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
