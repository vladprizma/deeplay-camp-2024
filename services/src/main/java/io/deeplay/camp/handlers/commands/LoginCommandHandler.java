package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.entity.User;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.user.UserService;
import io.deeplay.camp.token.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Handler for processing login commands.
 * <p>
 * This handler is responsible for processing login requests from clients. It validates the input message,
 * checks the user's credentials, and generates refresh tokens if the login is successful. It also logs the
 * process and handles any unexpected errors that may occur.
 * </p>
 */
public class LoginCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginCommandHandler.class);

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public LoginCommandHandler() {
        this.userService = new UserService();
        this.refreshTokenService = new RefreshTokenService();
    }

    /**
     * Handles the login command.
     * <p>
     * This method validates the input parameters, retrieves the user information from the database,
     * verifies the user's password, and generates refresh tokens if the login is successful. It also logs
     * the process and handles any unexpected errors that may occur.
     * </p>
     *
     * @param message     the message received from the client, should not be null
     * @param mainHandler the main handler managing the session, should not be null
     * @throws IOException  if an unexpected error occurs during the handling process
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        // Validate input parameters
        if (message == null || mainHandler == null) {
            logger.error("Message or MainHandler is null");
            throw new IllegalArgumentException("Message or MainHandler cannot be null");
        }

        String[] parts = message.split(MainHandler.splitRegex);
        if (parts.length < 3) {
            logger.error("Invalid message format: {}", message);
            mainHandler.sendMessageToClient("Invalid message format");
            return;
        }

        String username = parts[1];
        String password = parts[2];

        try {
            Optional<User> optionalUser = userService.getUserByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (userService.verifyPassword(password, user.getPassword())) {
                    mainHandler.setUser(user);
                    mainHandler.sendMessageToClient("Login successful. Welcome, " + username);
                    var tokens = refreshTokenService.generateRefreshToken(user);
                    var updateToken = tokens.updateToken;
                    var refreshToken = tokens.refreshToken;
                    mainHandler.setLogin(true);
                    mainHandler.sendMessageToClient("login::" + refreshToken + "::" + updateToken);
                    logger.info("User {} logged in successfully", username);
                } else {
                    mainHandler.sendMessageToClient("Invalid password.");
                    logger.warn("Invalid password attempt for user {}", username);
                }
            } else {
                mainHandler.sendMessageToClient("User not found.");
                logger.warn("User {} not found", username);
            }
        } catch (SQLException e) {
            logger.error("Database error occurred while processing login for user {}", username, e);
            mainHandler.sendMessageToClient("Internal server error. Please try again later.");
        }
    }
}