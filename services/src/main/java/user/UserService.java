package user;

import board.BoardLogic;
import entity.User;
import enums.Color;
import io.deeplay.camp.dao.UserDAO;
import password.PasswordService;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public int addUser(User user) throws SQLException {
        return userDAO.addUser(user);
    }
    
    public Optional<User> getUserByUsername(String username) throws SQLException {
        return userDAO.getUserByUsername(username);
    }
    
    public boolean verifyPassword(String password, String userPassword) {
        return PasswordService.checkPassword(password, userPassword);
    }
}
