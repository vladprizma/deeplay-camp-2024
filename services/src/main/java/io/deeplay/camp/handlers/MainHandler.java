package io.deeplay.camp.handlers;

import entity.Board;
import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.chat.ChatService;
import entity.ChatMessage;
import entity.GameSession;
import entity.User;
import enums.GameStatus;
import io.deeplay.camp.Main;
import io.deeplay.camp.game.GameLogic;
import io.deeplay.camp.handlers.commands.*;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.password.PasswordService;
import io.deeplay.camp.repository.CommandHandler;
import io.deeplay.camp.token.JwtService;
import io.deeplay.camp.token.RefreshTokenService;
import io.deeplay.camp.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handles communication with a connected client.
 */
public class MainHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GameSession session;
    private User user;
    public static String splitRegex = " ";
    private boolean isLogin = false;
    private GameLogic gameLogic;
    private BoardLogic boardLogic;
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    /**
     * Constructor for ClientHandler.
     *
     * @param socket the socket connected to the client
     */
    public MainHandler(Socket socket) {
        this.socket = socket;

        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        commandHandlers.put("login", new LoginCommandHandler());
        commandHandlers.put("register", new RegisterCommandHandler());
        commandHandlers.put("start", new StartCommandHandler());
        commandHandlers.put("session-start", new SessionStartCommandHandler());
        commandHandlers.put("send-message", new SendMessageCommandHandler());
        commandHandlers.put("get-messages", new GetMessagesCommandHandler());
        commandHandlers.put("pause", new PauseCommandHandler());
        commandHandlers.put("disconnect", new DisconnectCommandHandler());
        commandHandlers.put("move", new MoveCommandHandler());
        commandHandlers.put("session-chat", new SessionChatCommandHandler());
    }

    /**
     * The main logic for handling the client's connection.
     */
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            SessionManager.getInstance().addHandler(this);

            logger.info("Waiting for session.");
            sendMessageToClient("Waiting for session.");

            String message;

            while ((message = in.readLine()) != null) {
                String command = message.split(splitRegex)[0];
                CommandHandler handler = commandHandlers.get(command);

                if (handler != null) {
                    handler.handle(message, this);
                } else {
                    logger.info("Empty request or bad request: {}", message);
                    sendMessageToClient("Empty request or bad request");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameSession getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
    
    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }
    
    public GameLogic getGameLogic() {
        return this.gameLogic;
    }
    
    public void setBoardLogic(BoardLogic boardLogic) {
        this.boardLogic = boardLogic;
    }
    
    public BoardLogic getBoardLogic() {
        return boardLogic;
    }
    
    public void setBoard(Board board) {
        session.setBoard(board);
    }
    
    public Board getBoard() {
        return session.getBoard();
    }
    
    /**
     * Sends a message to the client.
     *
     * @param msg the message to send
     */
    public void sendMessageToClient(String msg) {
        out.println(msg);
    }

    /**
     * Gets the user associated with this handler.
     *
     * @return the user
     */
    public User getHandlerPlayer() {
        return user;
    }

    /**
     * Gets the session associated with this handler.
     *
     * @return the game session
     */
    public GameSession getHandlerSession() {
        return session;
    }

    /**
     * Gets the socket associated with this handler.
     *
     * @return the socket
     */
    public Socket getHandlerSocket() {
        return socket;
    }

    /**
     * Closes the connection and cleans up resources.
     */
    public void closeConnection() {
        try {
            logger.info("Player disconnect.");
            SessionManager.getInstance().deleteHandler(this);

            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
