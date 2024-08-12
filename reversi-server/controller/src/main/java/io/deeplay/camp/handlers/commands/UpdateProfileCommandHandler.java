package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.entity.User;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.password.PasswordService;
import io.deeplay.camp.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * CommandHandler for updating a user's profile.
 * <p>
 * This handler allows updating the username, password, and profile photo of a user.
 * It validates the input message, updates the user information in the database, and
 * sends appropriate responses to the client. It also logs the process and handles any
 * unexpected errors that may occur.
 * </p>
 */
public class UpdateProfileCommandHandler implements CommandHandler {

    private final UserService userService = new UserService();
    private static final Logger logger = LoggerFactory.getLogger(UpdateProfileCommandHandler.class);

    /**
     * Handles the command to update a user's profile.
     * <p>
     * This method validates the input parameters, updates the username, password, and profile photo
     * of the user in the database, and sends appropriate responses to the client. In case of errors,
     * appropriate messages are sent to the client and the error is logged.
     * </p>
     *
     * @param message     The command message containing the new profile information.
     * @param mainHandler The main handler managing the session.
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        String[] parts = message.split(MainHandler.splitRegex);
        if (parts.length < 4) {
            mainHandler.sendMessageToClient("Invalid request. Expected format: update-profile <username> <password> <photo>");
            return;
        }

        String newUsername = parts[1];
        String newPassword = parts[2];
        String newUserPhoto = parts[3];

        User user = mainHandler.getUser();
        if (user == null) {
            mainHandler.sendMessageToClient("Please login first.");
            return;
        }

        boolean isUpdated = false;

        if (!newUsername.equals(user.getUsername())) {
            user.setUsername(newUsername);
            userService.updateUsername(user.getId(), newUsername);
            isUpdated = true;
            logger.info("User ID {}: Username updated to {}", user.getId(), newUsername);
        }

        if (!newPassword.isEmpty()) {
            user.setPassword(PasswordService.hashPassword(newPassword));
            userService.updatePassword(user.getId(), user.getPassword());
            isUpdated = true;
            logger.info("User ID {}: Password updated", user.getId());
        }

        if (!newUserPhoto.isEmpty()) {
            user.setUserPhoto(newUserPhoto);
            userService.updateUserPhoto(user.getId(), newUserPhoto);
            isUpdated = true;
            logger.info("User ID {}: User photo updated", user.getId());
        }

        if (isUpdated) {
            mainHandler.sendMessageToClient("Profile updated successfully.");
        } else {
            mainHandler.sendMessageToClient("No changes were made to the profile.");
        }
    }
}