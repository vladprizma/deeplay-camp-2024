package io.deeplay.camp.botfactory.model;

/**
 * Represents the state of a finished game.
 */
public class GameFinished {
    private boolean isGameFinished;
    private int userIdWinner;

    /**
     * Constructs a new GameFinished instance with the specified game state and winner ID.
     *
     * @param isGameFinished Indicates whether the game is finished.
     * @param userIdWinner The ID of the user who won the game.
     */
    public GameFinished(boolean isGameFinished, int userIdWinner) {
        this.isGameFinished = isGameFinished;
        this.userIdWinner = userIdWinner;
    }

    /**
     * Checks if the game is finished.
     *
     * @return true if the game is finished, false otherwise.
     */
    public boolean isGameFinished() {
        return isGameFinished;
    }

    /**
     * Gets the ID of the user who won the game.
     *
     * @return The ID of the user who won the game.
     */
    public int getUserIdWinner() {
        return userIdWinner;
    }

    /**
     * Sets the game finished state.
     *
     * @param gameFinished The new game finished state.
     */
    public void setGameFinished(boolean gameFinished) {
        isGameFinished = gameFinished;
    }

    /**
     * Sets the ID of the user who won the game.
     *
     * @param userIdWinner The new ID of the user who won the game.
     */
    public void setUserIdWinner(int userIdWinner) {
        this.userIdWinner = userIdWinner;
    }
}