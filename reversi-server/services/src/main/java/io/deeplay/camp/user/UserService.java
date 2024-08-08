package io.deeplay.camp.user;

import io.deeplay.camp.entity.User;
import io.deeplay.camp.dao.UserDAO;
import io.deeplay.camp.password.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Service for handling user-related operations.
 * <p>
 * This class provides methods for adding users, retrieving users by username, verifying passwords, and updating user details such as rating, username, password, and user photo.
 * It uses the UserDAO for database operations and PasswordService for password verification.
 * </p>
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;

    /**
     * Initializes a new UserService with a UserDAO.
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Adds a new user to the database.
     *
     * @param user The user to be added.
     * @return The ID of the added user.
     * @throws SQLException If a SQL error occurs during the operation.
     */
    public int addUser(User user) throws SQLException {
        logger.info("Adding new user: {}", user.getUsername());
        return userDAO.addUser(user);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to be retrieved.
     * @return An Optional containing the user if found, or an empty Optional if not found.
     * @throws SQLException If a SQL error occurs during the operation.
     */
    public Optional<User> getUserByUsername(String username) throws SQLException {
        logger.info("Retrieving user by username: {}", username);
        return userDAO.getUserByUsername(username);
    }

    /**
     * Verifies if the provided password matches the stored user password.
     *
     * @param password     The plain text password to be verified.
     * @param userPassword The hashed password stored for the user.
     * @return True if the passwords match, false otherwise.
     */
    public boolean verifyPassword(String password, String userPassword) {
        logger.info("Verifying password for user");
        return PasswordService.checkPassword(password, userPassword);
    }

    /**
     * Updates the rating of a user.
     *
     * @param userId The ID of the user whose rating is to be updated.
     * @param rating The new rating to be set.
     * @throws SQLException If a SQL error occurs during the operation or if the user is not found.
     */
    public void updateRating(int userId, int rating) throws SQLException {
        logger.info("Updating rating for user ID: {}", userId);
        User user = userDAO.getUserById(userId).orElseThrow(() -> new SQLException("User not found"));
        user.setRating(rating);
        userDAO.updateUser(user);
    }

    /**
     * Updates the username of a user.
     *
     * @param userId      The ID of the user whose username is to be updated.
     * @param newUsername The new username to be set.
     * @throws SQLException If a SQL error occurs during the operation or if the user is not found.
     */
    public void updateUsername(int userId, String newUsername) throws SQLException {
        logger.info("Updating username for user ID: {}", userId);
        User user = userDAO.getUserById(userId).orElseThrow(() -> new SQLException("User not found"));
        user.setUsername(newUsername);
        userDAO.updateUser(user);
    }

    /**
     * Updates the password of a user.
     *
     * @param userId      The ID of the user whose password is to be updated.
     * @param newPassword The new password to be set.
     * @throws SQLException If a SQL error occurs during the operation or if the user is not found.
     */
    public void updatePassword(int userId, String newPassword) throws SQLException {
        logger.info("Updating password for user ID: {}", userId);
        User user = userDAO.getUserById(userId).orElseThrow(() -> new SQLException("User not found"));
        user.setPassword(newPassword);
        userDAO.updateUser(user);
    }

    /**
     * Updates the user photo of a user.
     *
     * @param userId      The ID of the user whose user photo is to be updated.
     * @param newUserPhoto The new user photo to be set.
     * @throws SQLException If a SQL error occurs during the operation or if the user is not found.
     */
    public void updateUserPhoto(int userId, String newUserPhoto) throws SQLException {
        logger.info("Updating user photo for user ID: {}", userId);
        User user = userDAO.getUserById(userId).orElseThrow(() -> new SQLException("User not found"));
        user.setUserPhoto(newUserPhoto);
        userDAO.updateUser(user);
    }

    /**
     * Checks if the given username is unique.
     *
     * @param username The username to be checked.
     * @return True if the username is unique, false otherwise.
     * @throws SQLException If a SQL error occurs during the operation.
     */
    public boolean isUsernameUnique(String username) throws SQLException {
        logger.info("Checking if username is unique: {}", username);
        return userDAO.getUserByUsername(username).isEmpty();
    }
}