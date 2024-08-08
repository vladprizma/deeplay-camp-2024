package io.deeplay.camp.handlers.main;

import io.deeplay.camp.entity.Board;
import io.deeplay.camp.entity.GameSession;
import io.deeplay.camp.entity.User;
import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.game.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the context of a game session.
 * <p>
 * This class holds the state of a game session, including the user, game logic, and board logic.
 * It provides methods to get and set these states, ensuring that the game session is managed correctly.
 * It also logs the process and handles any unexpected errors that may occur.
 * </p>
 */
public class GameContext {
    private static final Logger logger = LoggerFactory.getLogger(GameContext.class);
    private GameSession session;
    private User user;
    private boolean isLogin = false;
    private GameService gameLogic;
    private BoardService boardLogic;

    /**
     * Retrieves the current game session.
     *
     * @return The current game session.
     */
    public GameSession getSession() {
        return session;
    }

    /**
     * Sets the current game session.
     *
     * @param session The game session to be set.
     */
    public void setSession(GameSession session) {
        this.session = session;
        logger.info("Game session set: {}", session);
    }

    /**
     * Retrieves the current user.
     *
     * @return The current user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the current user.
     *
     * @param user The user to be set.
     */
    public void setUser(User user) {
        this.user = user;
        logger.info("User set: {}", user);
    }

    /**
     * Checks if the user is logged in.
     *
     * @return True if the user is logged in, false otherwise.
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * Sets the login status of the user.
     *
     * @param login The login status to be set.
     */
    public void setLogin(boolean login) {
        isLogin = login;
        logger.info("Login status set to: {}", login);
    }

    /**
     * Retrieves the current game logic.
     *
     * @return The current game logic.
     */
    public GameService getGameLogic() {
        return gameLogic;
    }

    /**
     * Sets the current game logic.
     *
     * @param gameLogic The game logic to be set.
     */
    public void setGameLogic(GameService gameLogic) {
        this.gameLogic = gameLogic;
        logger.info("Game logic set: {}", gameLogic);
    }

    /**
     * Retrieves the current board logic.
     *
     * @return The current board logic.
     */
    public BoardService getBoardLogic() {
        return boardLogic;
    }

    /**
     * Sets the current board logic.
     *
     * @param boardLogic The board logic to be set.
     */
    public void setBoardLogic(BoardService boardLogic) {
        this.boardLogic = boardLogic;
        logger.info("Board logic set: {}", boardLogic);
    }

    /**
     * Sets the board for the current game session.
     *
     * @param board The board to be set.
     */
    public void setBoard(Board board) {
        session.setBoard(board);
        logger.info("Board set for session: {}", board);
    }

    /**
     * Retrieves the board for the current game session.
     *
     * @return The current board.
     */
    public Board getBoard() {
        return session.getBoard();
    }
}