package io.deeplay.camp.handlers.commands;

import entity.User;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.repository.CommandHandler;
import io.deeplay.camp.token.JwtService;
import io.deeplay.camp.user.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Обработчик команды для начала сессии пользователя.
 */
public class SessionStartCommandHandler implements CommandHandler {

    private final UserService userService = new UserService();
    private final JwtService jwtService = new JwtService();

    /**
     * Обрабатывает команду начала сессии.
     *
     * @param message Сообщение с JWT токеном.
     * @param mainHandler Основной обработчик, управляющий сессией.
     * @throws IOException В случае ошибки ввода-вывода.
     * @throws SQLException В случае ошибки работы с базой данных.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        String[] parts = message.split(MainHandler.splitRegex);
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
            } else {
                mainHandler.sendMessageToClient("Invalid or expired JWT token. Please login again.");
            }
        } else {
            mainHandler.sendMessageToClient("User not found with jwt token.");
        }
    }
}