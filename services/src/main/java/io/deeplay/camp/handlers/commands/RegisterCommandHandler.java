package io.deeplay.camp.handlers.commands;

import entity.User;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.repository.CommandHandler;
import io.deeplay.camp.user.UserService;
import io.deeplay.camp.password.PasswordService;
import io.deeplay.camp.token.RefreshTokenService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CommandHandler для регистрации новых пользователей.
 */
public class RegisterCommandHandler implements CommandHandler {

    private static final Logger logger = Logger.getLogger(RegisterCommandHandler.class.getName());
    private final UserService userService = new UserService();
    private final RefreshTokenService refreshTokenService = new RefreshTokenService();

    /**
     * Обрабатывает команду для регистрации нового пользователя.
     *
     * @param message Сообщение команды.
     * @param mainHandler Основной обработчик, управляющий сессией.
     * @throws IOException В случае ошибки ввода-вывода.
     * @throws SQLException В случае ошибки SQL.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        logger.info("Handling register command");

        String[] parts = message.split(MainHandler.splitRegex);
        if (parts.length < 4) {
            String errorMsg = "Invalid message format. Expected: register::username::password::userPhoto";
            logger.warning(errorMsg);
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
            logger.log(Level.SEVERE, "Error handling register command", e);
            throw e; 
        }
    }
}
