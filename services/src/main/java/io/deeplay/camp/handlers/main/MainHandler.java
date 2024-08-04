package io.deeplay.camp.handlers.main;

import entity.Board;
import entity.GameSession;
import entity.User;
import io.deeplay.camp.Main;
import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.game.GameLogic;
import io.deeplay.camp.handlers.commands.*;
import io.deeplay.camp.managers.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class MainHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final ConnectionManager connectionManager;
    private final GameContext gameContext;
    private final CommandDispatcher commandDispatcher;

    public static String splitRegex = " ";

    public MainHandler(Socket socket) throws IOException {
        this.connectionManager = new ConnectionManager(socket);
        this.gameContext = new GameContext();
        this.commandDispatcher = new CommandDispatcher();

        registerCommandHandlers();
    }

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
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    // Delegated methods for GameContext
    public User getUser() {
        return gameContext.getUser();
    }

    public void setUser(User user) {
        gameContext.setUser(user);
    }

    public GameSession getSession() {
        return gameContext.getSession();
    }

    public void setSession(GameSession session) {
        gameContext.setSession(session);
    }

    public boolean isLogin() {
        return gameContext.isLogin();
    }

    public void setLogin(boolean login) {
        gameContext.setLogin(login);
    }

    public GameLogic getGameLogic() {
        return gameContext.getGameLogic();
    }

    public void setGameLogic(GameLogic gameLogic) {
        gameContext.setGameLogic(gameLogic);
    }

    public BoardLogic getBoardLogic() {
        return gameContext.getBoardLogic();
    }

    public void setBoardLogic(BoardLogic boardLogic) {
        gameContext.setBoardLogic(boardLogic);
    }

    public Board getBoard() {
        return gameContext.getBoard();
    }

    public void setBoard(Board board) {
        gameContext.setBoard(board);
    }

    // Delegated methods for ConnectionManager
    public void sendMessageToClient(String msg) {
        connectionManager.sendMessageToClient(msg);
    }

    public Socket getHandlerSocket() {
        return connectionManager.getSocket();
    }

    public void closeConnection() {
        logger.info("Player disconnect.");
        SessionManager.getInstance().deleteHandler(this);
        connectionManager.closeConnection();
    }

    public Logger getLogger() {
        return logger;
    }
}
