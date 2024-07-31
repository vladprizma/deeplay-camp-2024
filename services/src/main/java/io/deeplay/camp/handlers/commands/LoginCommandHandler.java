package io.deeplay.camp.handlers.commands;

import entity.User;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.repository.CommandHandler;
import io.deeplay.camp.user.UserService;
import io.deeplay.camp.token.RefreshTokenService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class LoginCommandHandler implements CommandHandler {

    private final UserService userService = new UserService();
    private final RefreshTokenService refreshTokenService = new RefreshTokenService();

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        String[] parts = message.split(MainHandler.splitRegex);
        String username = parts[1];
        String password = parts[2];

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
                mainHandler.sendMessageToClient( "login::" + refreshToken + "::" + updateToken);
            } else {
                mainHandler.sendMessageToClient("Invalid password.");
            }
        } else {
            mainHandler.sendMessageToClient("User not found.");
        }
    }
}
