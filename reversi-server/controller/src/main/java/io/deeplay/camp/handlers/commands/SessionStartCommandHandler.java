package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.entity.User;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.token.JwtService;
import io.deeplay.camp.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * CommandHandler for starting a user session.
 * <p>
 * This handler is responsible for processing commands to start a user session. It validates the JWT token,
 * retrieves the user associated with the token, and sets the user in the main handler. It also logs the process
 * and handles any unexpected errors that may occur.
 * </p>
 */
public class SessionStartCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(SessionStartCommandHandler.class);
    private final UserService userService = new UserService();
    private final JwtService jwtService = new JwtService();

    /**
     * Handles the command to start a user session.
     * <p>
     * This method validates the JWT token, retrieves the user associated with the token, and sets the user in the main handler.
     * In case of errors, appropriate messages are sent to the client and the error is logged.
     * </p>
     *
     * @param message     The message containing the JWT token.
     * @param mainHandler The main handler managing the session.
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        logger.info("Handling session start command");

        String[] parts = message.split(MainHandler.splitRegex);
        if (parts.length < 2) {
            String errorMsg = "Invalid message format. Expected: session-start::jwtToken";
            logger.warn(errorMsg);
            mainHandler.sendMessageToClient(errorMsg);
            return;
        }

        String jwtToken = parts[1];
        String username = jwtService.extractUsername(jwtToken);

        Optional<User> optionalUser = userService.getUserByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (jwtService.isTokenValid(jwtToken, user)) {
                mainHandler.setUser(user);
                mainHandler.sendMessageToClient("Session started successfully. Welcome back, " + username);
                mainHandler.sendMessageToClient("session-start::" + user.getUsername() + "::" + user.getUserPhoto() + "::" + user.getMatches() + "::" + user.getRating());
                mainHandler.setLogin(true);
                logger.info("Session started successfully for user: " + username);
            } else {
                String errorMsg = "Invalid or expired JWT token. Please login again.";
                logger.warn(errorMsg);
                mainHandler.sendMessageToClient(errorMsg);
            }
        } else {
            String errorMsg = "User not found with jwt token.";
            logger.warn(errorMsg);
            mainHandler.sendMessageToClient(errorMsg);
        }
    }
}