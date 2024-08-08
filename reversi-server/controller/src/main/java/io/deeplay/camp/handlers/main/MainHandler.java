package io.deeplay.camp.handlers.main;

import io.deeplay.camp.entity.Board;
import io.deeplay.camp.entity.GameSession;
import io.deeplay.camp.entity.User;
import io.deeplay.camp.Main;
import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.game.GameService;
import io.deeplay.camp.handlers.commands.*;
import io.deeplay.camp.managers.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

/**
 * MainHandler is responsible for managing the main operations of a game session.
 * <p>
 * This class handles the connection with the client, dispatches commands to their respective handlers,
 * and manages the game context. It also logs the process and handles any unexpected errors that may occur.
 * </p>
 */
public class MainHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final ConnectionManager connectionManager;
    private final GameContext gameContext;
    private final CommandDispatcher commandDispatcher;

    public static String splitRegex = " ";

    /**
     * Initializes a new MainHandler with the given socket.
     * <p>
     * This constructor sets up the connection manager, game context, and command dispatcher.
     * It also registers all the command handlers.
     * </p>
     *
     * @param socket The socket representing the connection to the client.
     * @throws IOException If an I/O error occurs when creating the input or output streams.
     */
    public MainHandler(Socket socket) throws IOException {
        this.connectionManager = new ConnectionManager(socket);
        this.gameContext = new GameContext();
        this.commandDispatcher = new CommandDispatcher();

        registerCommandHandlers();
    }

    /**
     * Registers all the command handlers.
     * <p>
     * This method adds all the command handlers to the command dispatcher.
     * </p>
     */
    private void registerCommandHandlers() {
        commandDispatcher.registerCommandHandler("login", new LoginCommandHandler());
        commandDispatcher.registerCommandHandler("register", new RegisterCommandHandler());
        commandDispatcher.registerCommandHandler("game-start", new StartCommandHandler());
        commandDispatcher.registerCommandHandler("session-start", new SessionStartCommandHandler());
        commandDispatcher.registerCommandHandler("send-global-message", new SendMessageCommandHandler());
        commandDispatcher.registerCommandHandler("get-global-messages", new GetMessagesCommandHandler());
        commandDispatcher.registerCommandHandler("game-pause", new PauseCommandHandler());
        commandDispatcher.registerCommandHandler("game-disconnect", new DisconnectCommandHandler());
        commandDispatcher.registerCommandHandler("game-move", new MoveCommandHandler());
        commandDispatcher.registerCommandHandler("send-message-session-chat", new SendSessionChatCommandHandler());
        commandDispatcher.registerCommandHandler("get-messages-session-chat", new GetSessionChatCommandHandler());
        commandDispatcher.registerCommandHandler("get-board", new GetBoardCommandHandler());
        commandDispatcher.registerCommandHandler("get-valid-moves", new GetValidMovesCommandHandler());
        commandDispatcher.registerCommandHandler("update-user-profile", new UpdateProfileCommandHandler());
    }

    /**
     * The main run method for the handler.
     * <p>
     * This method waits for messages from the client and dispatches them to the appropriate command handler.
     * It also logs the process and handles any unexpected errors that may occur.
     * </p>
     */
    @Override
    public void run() {
        try {
            SessionManager.getInstance().addHandler(this);

            logger.info("Waiting for session.");
            connectionManager.sendMessageToClient("Waiting for session.");

            String message;

            while ((message = connectionManager.getInputReader().readLine()) != null) {
                commandDispatcher.dispatchCommand(message, this);
            }
        } catch (IOException | SQLException | InterruptedException e) {
            logger.error("Error in MainHandler run method", e);
        } finally {
            closeConnection();
        }
    }

    // Delegated methods for GameContext

    /**
     * Retrieves the current user.
     *
     * @return The current user.
     */
    public User getUser() {
        return gameContext.getUser();
    }

    /**
     * Sets the current user.
     *
     * @param user The user to be set.
     */
    public void setUser(User user) {
        gameContext.setUser(user);
        logger.info("User set: {}", user);
    }

    /**
     * Retrieves the current game session.
     *
     * @return The current game session.
     */
    public GameSession getSession() {
        return gameContext.getSession();
    }

    /**
     * Sets the current game session.
     *
     * @param session The game session to be set.
     */
    public void setSession(GameSession session) {
        gameContext.setSession(session);
        logger.info("Game session set: {}", session);
    }

    /**
     * Checks if the user is logged in.
     *
     * @return True if the user is logged in, false otherwise.
     */
    public boolean isLogin() {
        return gameContext.isLogin();
    }

    /**
     * Sets the login status of the user.
     *
     * @param login The login status to be set.
     */
    public void setLogin(boolean login) {
        gameContext.setLogin(login);
        logger.info("Login status set to: {}", login);
    }

    /**
     * Retrieves the current game logic.
     *
     * @return The current game logic.
     */
    public GameService getGameLogic() {
        return gameContext.getGameLogic();
    }

    /**
     * Sets the current game logic.
     *
     * @param gameLogic The game logic to be set.
     */
    public void setGameLogic(GameService gameLogic) {
        gameContext.setGameLogic(gameLogic);
        logger.info("Game logic set: {}", gameLogic);
    }

    /**
     * Retrieves the current board logic.
     *
     * @return The current board logic.
     */
    public BoardService getBoardLogic() {
        return gameContext.getBoardLogic();
    }

    /**
     * Sets the current board logic.
     *
     * @param boardLogic The board logic to be set.
     */
    public void setBoardLogic(BoardService boardLogic) {
        gameContext.setBoardLogic(boardLogic);
        logger.info("Board logic set: {}", boardLogic);
    }

    /**
     * Sets the board for the current game session.
     *
     * @param board The board to be set.
     */
    public void setBoard(Board board) {
        gameContext.setBoard(board);
        logger.info("Board set for session: {}", board);
    }

    /**
     * Retrieves the board for the current game session.
     *
     * @return The current board.
     */
    public Board getBoard() {
        return gameContext.getBoard();
    }

    // Delegated methods for ConnectionManager

    /**
     * Sends a message to the client.
     *
     * @param msg The message to be sent to the client.
     */
    public void sendMessageToClient(String msg) {
        connectionManager.sendMessageToClient(msg);
        logger.info("Sent message to client: {}", msg);
    }

    /**
     * Retrieves the socket for the connection.
     *
     * @return The socket for the connection.
     */
    public Socket getHandlerSocket() {
        return connectionManager.getSocket();
    }

    /**
     * Closes the connection to the client.
     * <p>
     * This method closes the socket and the input and output streams. It also logs the process and handles any
     * unexpected errors that may occur.
     * </p>
     */
    public void closeConnection() {
        logger.info("Player disconnect.");
        SessionManager.getInstance().deleteHandler(this);
        connectionManager.closeConnection();
    }

    /**
     * Retrieves the logger for the handler.
     *
     * @return The logger for the handler.
     */
    public Logger getLogger() {
        return logger;
    }
}