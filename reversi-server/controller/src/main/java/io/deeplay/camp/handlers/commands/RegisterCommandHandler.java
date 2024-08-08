package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.entity.User;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.user.UserService;
import io.deeplay.camp.password.PasswordService;
import io.deeplay.camp.token.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * CommandHandler for registering new users.
 * <p>
 * This handler is responsible for processing registration commands from clients. It validates the input message,
 * checks if the username is unique, hashes the password, and stores the new user in the database. It also generates
 * refresh tokens for the new user and logs the process. In case of errors, appropriate messages are sent to the client.
 * </p>
 */
public class RegisterCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegisterCommandHandler.class);
    private final UserService userService = new UserService();
    private final RefreshTokenService refreshTokenService = new RefreshTokenService();

    /**
     * Handles the command to register a new user.
     * <p>
     * This method validates the input parameters, checks if the username is unique, hashes the password,
     * and stores the new user in the database. It also generates refresh tokens for the new user and logs the process.
     * In case of errors, appropriate messages are sent to the client.
     * </p>
     *
     * @param message     The command message.
     * @param mainHandler The main handler managing the session.
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        logger.info("Handling register command");

        String[] parts = message.split(MainHandler.splitRegex);
        if (parts.length < 4) {
            String errorMsg = "Invalid message format. Expected: register::username::password::userPhoto";
            logger.warn(errorMsg);
            mainHandler.sendMessageToClient(errorMsg);
            return;
        }

        if (!userService.isUsernameUnique(parts[1])) {
            String errorMsg = "Not unique username.";
            logger.warn(errorMsg);
            mainHandler.sendMessageToClient(errorMsg);
            return;
        }

        try {
            String username = parts[1];
            String password = parts[2];
            String userPhoto = parts[3];

            User user = new User(0, username, PasswordService.hashPassword(password), 0, 0, userPhoto);
            int userId = userService.addUser(user);
            user.setId(userId);
            mainHandler.setUser(user);

            mainHandler.sendMessageToClient("Registration successful. Welcome, " + username);
            mainHandler.setLogin(true);

            var tokens = refreshTokenService.generateRefreshToken(user);
            var updateToken = tokens.updateToken;
            var refreshToken = tokens.refreshToken;
            mainHandler.sendMessageToClient("register::" + refreshToken + "::" + updateToken);

            logger.info("User registered successfully: " + username);
        } catch (SQLException e) {
            logger.error("Error handling register command", e);
            throw e;
        }
    }
}