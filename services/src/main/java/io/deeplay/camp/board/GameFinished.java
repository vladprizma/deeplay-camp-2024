package io.deeplay.camp.board;

public class GameFinished {
    private boolean isGameFinished;
    private int userIdWinner;
    
    public GameFinished(boolean isGameFinished, int userIdWinner) {
        this.isGameFinished = isGameFinished;
        this.userIdWinner = userIdWinner;
    }


    public boolean isGameFinished() {
        return isGameFinished;
    }

    public int getUserIdWinner() {
        return userIdWinner;
    }

    public void setGameFinished(boolean gameFinished) {
        isGameFinished = gameFinished;
    }

    public void setUserIdWinner(int userIdWinner) {
        this.userIdWinner = userIdWinner;
    }
}
