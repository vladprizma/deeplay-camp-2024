package io.deeplay.camp.dao;

import io.deeplay.camp.entity.GameResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing game results in the database.
 * <p>
 * This class provides methods to add, retrieve, and delete game results from the database.
 * </p>
 */
public class ResultsDAO {
    private static final String DB_URL = "jdbc:postgresql://localhost:5431/reversi";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    /**
     * Adds a new game result to the database.
     *
     * @param result The game result to be added.
     * @throws SQLException If a database access error occurs.
     */
    public void addResult(GameResult result) throws SQLException {
        String sql = "INSERT INTO game_results (round, x, y, score) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, result.getRound());
            statement.setInt(2, result.getX());
            statement.setInt(3, result.getY());
            statement.setDouble(4, result.getScore());
            statement.executeUpdate();
        }
    }

    /**
     * Retrieves all game results from the database.
     *
     * @return A list of all game results.
     * @throws SQLException If a database access error occurs.
     */
    public List<GameResult> getAllResults() throws SQLException {
        String sql = "SELECT * FROM game_results ORDER BY round";
        List<GameResult> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int round = resultSet.getInt("round");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                double score = resultSet.getDouble("score");

                results.add(new GameResult(round, x, y, score));
            }
        }

        return results;
    }

    /**
     * Retrieves game results for a specific round.
     *
     * @param round The round number for which to retrieve results.
     * @return A list of game results for the specified round.
     * @throws SQLException If a database access error occurs.
     */
    public List<GameResult> getResultsByRound(int round) throws SQLException {
        String sql = "SELECT * FROM game_results WHERE round = ? ORDER BY x, y";
        List<GameResult> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, round);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int x = resultSet.getInt("x");
                    int y = resultSet.getInt("y");
                    double score = resultSet.getDouble("score");

                    results.add(new GameResult(round, x, y, score));
                }
            }
        }

        return results;
    }

    /**
     * Deletes a game result from the database by its round number and position (x, y).
     *
     * @param round The round number of the game result to be deleted.
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteResult(int round, int x, int y) throws SQLException {
        String sql = "DELETE FROM game_results WHERE round = ? AND x = ? AND y = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, round);
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.executeUpdate();
        }
    }
}