package io.deeplay.camp.handlers;

import entity.Board;
import entity.GameSession;
import entity.User;
import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.game.GameLogic;

public class GameContext {
    private GameSession session;
    private User user;
    private boolean isLogin = false;
    private GameLogic gameLogic;
    private BoardLogic boardLogic;

    public GameSession getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public BoardLogic getBoardLogic() {
        return boardLogic;
    }

    public void setBoardLogic(BoardLogic boardLogic) {
        this.boardLogic = boardLogic;
    }

    public void setBoard(Board board) {
        session.setBoard(board);
    }

    public Board getBoard() {
        return session.getBoard();
    }
}
