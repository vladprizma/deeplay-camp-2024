package io.deeplay.camp.entity;

/**
 * Represents a game result.
 * <p>
 * This class holds the details of a game result, including the round number,
 * position (x, y), and the score.
 * </p>
 */
public class GameResult {
    private int round;
    private int x;
    private int y;
    private double score;

    /**
     * Initializes a new GameResult with the specified details.
     *
     * @param round The round number.
     * @param x     The x-coordinate of the position.
     * @param y     The y-coordinate of the position.
     * @param score  The score associated with this result.
     */
    public GameResult(int round, int x, int y, double score) {
        this.round = round;
        this.x = x;
        this.y = y;
        this.score = score;
    }

    // Getters and Setters

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}