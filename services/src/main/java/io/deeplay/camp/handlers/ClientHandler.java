package io.deeplay.camp.handlers;

import io.deeplay.camp.chat.ChatService;
import entity.ChatMessage;
import entity.GameSession;
import entity.User;
import enums.GameStatus;
import io.deeplay.camp.Main;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.password.PasswordService;
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
import java.util.List;
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
    private static final String splitRegex = " ";
    private boolean isLogin = false;
    
    private final RefreshTokenService refreshTokenService = new RefreshTokenService();
    private final UserService userService = new UserService();
    private final JwtService jwtService = new JwtService();
    private final ChatService chatService = new ChatService();
    

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
                } else if (message.startsWith("session-start")) {
                    handleSessionStart(message);
                } else if (message.startsWith("send-message")) {
                    if (isLogin) handleSendMessage(message);
                    else sendMessageToClient("Please login or register.");
                } else if (message.startsWith("get-messages")) {
                    handleGetMessages();
                } else if (message.startsWith("start")) {
                    if (isLogin) handleStart();
                    else sendMessageToClient("Please login or register.");
                }  else if (message.equals("disconnect")) {
                    closeConnection();
                } else if (message.equals("pause") && session.getGameState() == GameStatus.IN_PROGRESS) {
                    SessionManager.getInstance().sendMessageToOpponent(this, session, user.getId() + " start pause");
                } else {
                    logger.info("Empty request or bad request");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void handleLogin(String message) {
        try {
            String[] parts = message.split(splitRegex);
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
            String[] parts = message.split(splitRegex);
            String username = parts[1];
            String password = parts[2];
            String userPhoto = parts[3];

            User user = new User(0, username, PasswordService.hashPassword(password), 0, 0, userPhoto);
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

    private void handleStart() {
        try {
            var result = SessionManager.getInstance().findOrCreateSession(this);
            session = result.getGameSession();
            while (session.getGameState() == GameStatus.NOT_STARTED) {
                Thread.sleep(1000);
            }

            logger.info(user.getId() + ": The enemy was found. The game begins...");
            sendMessageToClient(user.getId() + ": The enemy was found. The game begins...");
        } catch (InterruptedException e) {
            logger.error("Error during game start: ", e);
            sendMessageToClient("Game start failed due to server error.");
        }
    }
    
    private void handleSessionStart(String message) {
        try {
            String[] parts = message.split(splitRegex);
            String jwtToken = parts[1];
            String username = jwtService.extractUsername(jwtToken);
            Optional<User> optionalUser = userService.getUserByUsername(username);

            if (jwtService.isTokenValid(jwtToken, optionalUser.get())) {
                if (optionalUser.isPresent()) {
                    this.user = optionalUser.get();
                    sendMessageToClient("Session started successfully. Welcome back, " + username);
                    sendMessageToClient(user.getUsername() + "::" + user.getUserPhoto() + "::" + user.getMatches() + "::" + user.getRating());
                    isLogin = true;
                } else {
                    sendMessageToClient("User not found.");
                }
            } else {
                sendMessageToClient("Invalid or expired JWT token. Please login again.");
            }
        } catch (SQLException e) {
            logger.error("Error during session start: ", e);
            sendMessageToClient("Session start failed due to server error.");
        }
    }

    private void handleSendMessage(String message) {
        try {
            String[] parts = message.split(splitRegex, 2);
            String chatMessage = parts[1];

            chatService.addMessage(user.getId(), chatMessage);
            sendMessageToClient("Message sent successfully.");
        } catch (SQLException e) {
            logger.error("Error sending message: ", e);
            sendMessageToClient("Sending message failed due to server error.");
        }
    }

    private void handleGetMessages() {
        try {
            List<ChatMessage> messages = chatService.getAllMessages();
            StringBuilder response = new StringBuilder("Chat messages:");
            for (ChatMessage chatMessage : messages) {
                response.append("\n").append(chatMessage.getTimestamp())
                        .append(" - ").append(chatMessage.getUserId())
                        .append(": ").append(chatMessage.getMessage());
            }
            sendMessageToClient(response.toString());
        } catch (SQLException e) {
            logger.error("Error getting messages: ", e);
            sendMessageToClient("Getting messages failed due to server error.");
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
