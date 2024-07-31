package io.deeplay.camp.handlers.commands;

import entity.User;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.repository.CommandHandler;
import io.deeplay.camp.token.JwtService;
import io.deeplay.camp.user.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class SessionStartCommandHandler implements CommandHandler {

    private final UserService userService = new UserService();
    private final JwtService jwtService = new JwtService();

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        String[] parts = message.split(MainHandler.splitRegex);
        String jwtToken = parts[1];
        String username = jwtService.extractUsername(jwtToken);
        Optional<User> optionalUser = userService.getUserByUsername(username);

        if (jwtService.isTokenValid(jwtToken, optionalUser.get())) {
            if (optionalUser.isPresent()) {
                mainHandler.setUser(optionalUser.get());
                mainHandler.sendMessageToClient("Session started successfully. Welcome back, " + username);
                User user = mainHandler.getUser();
                mainHandler.sendMessageToClient(user.getUsername() + "::" + user.getUserPhoto() + "::" + user.getMatches() + "::" + user.getRating());
                mainHandler.setLogin(true);
            } else {
                mainHandler.sendMessageToClient("User not found.");
            }
        } else {
            mainHandler.sendMessageToClient("Invalid or expired JWT token. Please login again.");
        }
    }
}