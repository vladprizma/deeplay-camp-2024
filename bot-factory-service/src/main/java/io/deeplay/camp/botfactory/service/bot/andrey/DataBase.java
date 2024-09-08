package io.deeplay.camp.bot;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code DataBase} class is responsible for connecting to a PostgreSQL database,
 * creating necessary tables, and performing CRUD operations related to experience and win rate data.
 */
public class DataBase {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/model";
    private static final String USER = "postgres";
    private static final String PASS = "admin";
    private static final String TABLE_NAME = "experience_v_3";
    private static final String OpponentName = "random_bot";

    /**
     * Initializes a new instance of the {@code DataBase} class and creates necessary tables if they do not exist.
     */
    public DataBase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            if (conn != null) {
                createExperienceTable(conn);
                createWinRateTable(conn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates the 'experience' table if it does not exist.
     *
     * @param conn the database connection
     * @throws SQLException if a database access error occurs
     */
    private void createExperienceTable(Connection conn) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "id SERIAL PRIMARY KEY,"
                + "state BYTEA,"
                + "action INTEGER,"
                + "reward DOUBLE PRECISION,"
                + "nextState BYTEA,"
                + "done BOOLEAN"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    /**
     * Creates the 'win_rate' table if it does not exist.
     *
     * @param conn the database connection
     * @throws SQLException if a database access error occurs
     */
    private void createWinRateTable(Connection conn) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS win_rate_v3 ("
                + "id SERIAL PRIMARY KEY,"
                + "win_rate DOUBLE PRECISION,"
                + "opponent_name VARCHAR(255)"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    /**
     * Adds a new experience to the 'experience' table.
     *
     * @param experience the experience to be added
     */
    public void addExperience(Experience experience) {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (state, action, reward, nextState, done) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            Nd4j.write(bos, experience.getState());
            pstmt.setBytes(1, bos.toByteArray());

            pstmt.setInt(2, experience.getAction());
            pstmt.setDouble(3, experience.getReward());

            bos.reset();
            Nd4j.write(bos, experience.getNextState());
            pstmt.setBytes(4, bos.toByteArray());

            pstmt.setBoolean(5, experience.isDone());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of all experiences from the 'experience' table.
     *
     * @return a list of {@code Experience} objects
     */
    public List<Experience> getAllExperiences() {
        List<Experience> experiences = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME +
                "\nORDER BY random()\n" +
                "LIMIT 50000;";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                byte[] stateBytes = rs.getBytes("state");
                INDArray state = null;
                try (ByteArrayInputStream bos = new ByteArrayInputStream(stateBytes)) {
                    state = Nd4j.read(bos);
                } catch (IOException e) {
                    System.out.println("Error deserializing state INDArray: " + e.getMessage());
                }

                int action = rs.getInt("action");
                double reward = rs.getDouble("reward");

                byte[] nextStateBytes = rs.getBytes("nextState");
                INDArray nextState = null;
                try (ByteArrayInputStream bos1 = new ByteArrayInputStream(nextStateBytes)) {
                    nextState = Nd4j.read(bos1);
                } catch (IOException e) {
                    System.out.println("Error deserializing nextState INDArray: " + e.getMessage());
                }

                boolean done = rs.getBoolean("done");

                if (state != null && nextState != null) {
                    experiences.add(new Experience(state, action, reward, nextState, done, 1.0));
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return experiences;
    }

    /**
     * Calculates the win rate from the last 2000 rows of the 'experience' table and stores it in the 'win_rate' table.
     */
    public void calculateAndStoreWinRate() {
        String winRateSQL = "SELECT COUNT(*) AS total, " +
                "SUM(CASE WHEN reward > 0 THEN 1 ELSE 0 END) AS wins " +
                "FROM ( " +
                "SELECT * FROM " + TABLE_NAME +
                " ORDER BY id DESC " +
                "LIMIT 2000 " +
                ") AS last_2000_rows;";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(winRateSQL)) {

            if (rs.next()) {
                int total = rs.getInt("total");
                int wins = rs.getInt("wins");

                double winRate = (total > 0) ? (double) wins / total : 0;

                String insertWinRateSQL = "INSERT INTO win_rate_v3 (win_rate, opponent_name) VALUES (?, ?);";
                try (PreparedStatement pstmt = conn.prepareStatement(insertWinRateSQL)) {
                    pstmt.setDouble(1, winRate);
                    pstmt.setString(2, OpponentName);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    /**
     * Retrieves the latest win rate from the 'win_rate' table.
     *
     * @return the latest win rate, or -1.0 if retrieval fails
     */
    public double getLatestWinRate() {
        String querySQL = "SELECT win_rate FROM win_rate_v3 ORDER BY id DESC LIMIT 1;";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            if (rs.next()) {
                return rs.getDouble("win_rate");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        return -1.0;
    }
}