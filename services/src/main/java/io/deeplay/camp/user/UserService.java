package io.deeplay.camp.user;

import entity.User;
import io.deeplay.camp.dao.UserDAO;
import io.deeplay.camp.password.PasswordService;

import java.sql.SQLException;
import java.util.Optional;

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
    
    public void updateRating(int userId, int rating) throws SQLException {
        User user = userDAO.getUserById(userId).orElseThrow(() -> new SQLException("User not found"));
        user.setRating(rating);
        userDAO.updateUser(user);
    }

    public void updateUsername(int userId, String newUsername) throws SQLException {
        User user = userDAO.getUserById(userId).orElseThrow(() -> new SQLException("User not found"));
        user.setUsername(newUsername);
        userDAO.updateUser(user);
    }

    public void updatePassword(int userId, String newPassword) throws SQLException {
        User user = userDAO.getUserById(userId).orElseThrow(() -> new SQLException("User not found"));
        user.setPassword(newPassword);
        userDAO.updateUser(user);
    }

    public void updateUserPhoto(int userId, String newUserPhoto) throws SQLException {
        User user = userDAO.getUserById(userId).orElseThrow(() -> new SQLException("User not found"));
        user.setUserPhoto(newUserPhoto);
        userDAO.updateUser(user);
    }
}
