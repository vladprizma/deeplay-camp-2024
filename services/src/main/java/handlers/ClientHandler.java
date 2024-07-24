package handlers;

import entity.GameSession;
import entity.User;
import enums.GameStatus;
import io.deeplay.camp.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import managers.SessionManager;
import token.RefreshTokenService;
import user.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Handles communication with a connected client.
 */
public class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GameSession session;
    private User user;
    private RefreshTokenService refreshTokenService = new RefreshTokenService();
    private UserService userService = new UserService();

    /**
     * Constructor for ClientHandler.
     *
     * @param socket the socket connected to the client
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                if (message.startsWith("login")) {
                    handleLogin(message);
                } else if (message.startsWith("register")) {
                    handleRegister(message);
                } else if (message.startsWith("start")) {
                    var result = SessionManager.getInstance().findOrCreateSession(this);
                    session = result.getGameSession();
                    while (session.getGameState() == GameStatus.NOT_STARTED) {
                        Thread.sleep(1000);
                    }

                    logger.info(user.getId() + ": The enemy was found. The game begins...");
                    sendMessageToClient(user.getId() + ": The enemy was found. The game begins...");
                } else if (message.equals("disconnect")) {
                    closeConnection();
                } else if (message.equals("pause")) {
                    SessionManager.getInstance().sendMessageToOpponent(this, session, user.getId() + " start pause");
                } else {
                    logger.info("Empty request or bad request");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void handleLogin(String message) {
        try {
            // Parse login information from the message
            String[] parts = message.split(" ");
            String username = parts[1];
            String password = parts[2];

            Optional<User> optionalUser = userService.getUserByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (userService.verifyPassword(password, user.getPassword())) {
                    this.user = user;
                    sendMessageToClient("Login successful. Welcome, " + username);
                    var tokens = refreshTokenService.generateRefreshToken(user);
                    var updateToken = tokens.updateToken;
                    var refreshToken = tokens.refreshToken;
                    sendMessageToClient(refreshToken + "::" + updateToken);
                } else {
                    sendMessageToClient("Invalid password.");
                }
            } else {
                sendMessageToClient("User not found.");
            }
        } catch (SQLException e) {
            logger.error("Error during login: ", e);
            sendMessageToClient("Login failed due to server error.");
        }
    }

    private void handleRegister(String message) {
        try {
            // Parse registration information from the message
            String[] parts = message.split(" ");
            String username = parts[1];
            String password = parts[2];
            String userPhoto = parts[3];

            User user = new User(0, username, password, 0, 0, userPhoto);
            int userId = userService.addUser(user);
            user.setId(userId);
            this.user = user;

            sendMessageToClient("Registration successful. Welcome, " + username);

            var tokens = refreshTokenService.generateRefreshToken(user);
            var updateToken = tokens.updateToken;
            var refreshToken = tokens.refreshToken;
            sendMessageToClient(refreshToken + "::" + updateToken);
        } catch (SQLException e) {
            logger.error("Error during registration: ", e);
            sendMessageToClient("Registration failed due to server error.");
        }
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
    private void closeConnection() {
        try {
            logger.info("Игрок отключился.");
            SessionManager.getInstance().deleteHandler(this);

            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
