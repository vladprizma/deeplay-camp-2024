package io.deeplay.camp.handlers.commands;

import entity.User;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.repository.CommandHandler;
import io.deeplay.camp.user.UserService;
import io.deeplay.camp.password.PasswordService;
import io.deeplay.camp.token.RefreshTokenService;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterCommandHandler implements CommandHandler {

    private final UserService userService = new UserService();
    private final RefreshTokenService refreshTokenService = new RefreshTokenService();

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        String[] parts = message.split(MainHandler.splitRegex);
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
    }
}
