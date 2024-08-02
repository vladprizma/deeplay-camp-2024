package io.deeplay.camp.handlers.commands;


import entity.User;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.password.PasswordService;
import io.deeplay.camp.repository.CommandHandler;
import io.deeplay.camp.user.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateProfileCommandHandler implements CommandHandler {

    private final UserService userService = new UserService();

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        String[] parts = message.split(MainHandler.splitRegex);
        String newUsername = parts[1];
        String newPassword = parts[2];
        String newUserPhoto = parts[3];

        User user = mainHandler.getUser();
        if (user == null) {
            mainHandler.sendMessageToClient("Please login first.");
            return;
        }

        if (!newUsername.equals(user.getUsername())) {
            user.setUsername(newUsername);
            userService.updateUsername(user.getId(), newUsername);
        }

        if (!newPassword.isEmpty()) {
            user.setPassword(PasswordService.hashPassword(newPassword));
            userService.updatePassword(user.getId(), user.getPassword());
        }

        if (!newUserPhoto.isEmpty()) {
            user.setUserPhoto(newUserPhoto);
            userService.updateUserPhoto(user.getId(), newUserPhoto);
        }

        mainHandler.sendMessageToClient("Profile updated successfully.");
    }
}
