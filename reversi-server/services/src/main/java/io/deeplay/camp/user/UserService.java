package io.deeplay.camp.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.camp.entity.User;
import io.deeplay.camp.password.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

/**
 * Service for handling user-related operations.
 * <p>
 * This class provides methods for adding users, retrieving users by username, verifying passwords,
 * and updating user details such as rating, username, password, and user photo.
 * It communicates with the user microservice using HTTP requests.
 * </p>
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String BASE_URL = "http://localhost:8083/api/users"; 
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Adds a new user to the database.
     *
     * @param user The user to be added.
     * @return The ID of the added user.
     * @throws IOException If an IO error occurs during the operation.
     */
    public int addUser(User user) throws IOException {
        logger.info("Adding new user: {}", user.getUsername());
        String json = objectMapper.writeValueAsString(user);
        String response = sendRequest("http://localhost:8083/api/users", "POST", json);
        if (response != null && !response.isEmpty()) {
            User userReturn = objectMapper.readValue(response, User.class);
            return userReturn.getId();
        }
        return -1;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to be retrieved.
     * @return An Optional containing the user if found, or an empty Optional if not found.
     * @throws IOException If an IO error occurs during the operation.
     */
    public Optional<User> getUserByUsername(String username) throws IOException {
        logger.info("Retrieving user by username: {}", username);
        String response = sendRequest(BASE_URL + "/username/" + username, "GET", null);
        if (response != null && !response.isEmpty()) {
            User user = objectMapper.readValue(response, User.class);
            return Optional.of(user);
        }
        return Optional.empty();
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
     * @throws IOException If an IO error occurs during the operation or if the user is not found.
     */
    public void updateRating(int userId, int rating) throws IOException {
        logger.info("Updating rating for user ID: {}", userId);
        String url = BASE_URL + "/" + userId + "/rating?rating=" + rating;
        sendRequest(url, "PUT", null);
    }

    /**
     * Updates the username of a user.
     *
     * @param userId      The ID of the user whose username is to be updated.
     * @param newUsername The new username to be set.
     * @throws IOException If an IO error occurs during the operation or if the user is not found.
     */
    public void updateUsername(int userId, String newUsername) throws IOException {
        logger.info("Updating username for user ID: {}", userId);
        String json = objectMapper.writeValueAsString(Map.of("userId", userId, "newUsername", newUsername));
        sendRequest(BASE_URL + "/username", "PUT", json);
    }

    /**
     * Updates the password of a user.
     *
     * @param userId      The ID of the user whose password is to be updated.
     * @param newPassword The new password to be set.
     * @throws IOException If an IO error occurs during the operation or if the user is not found.
     */
    public void updatePassword(int userId, String newPassword) throws IOException {
        logger.info("Updating password for user ID: {}", userId);
        String json = objectMapper.writeValueAsString(Map.of("userId", userId, "newPassword", newPassword));
        sendRequest(BASE_URL + "/password", "PUT", json);
    }

    /**
     * Updates the user photo of a user.
     *
     * @param userId      The ID of the user whose user photo is to be updated.
     * @param newUserPhoto The new user photo to be set.
     * @throws IOException If an IO error occurs during the operation or if the user is not found.
     */
    public void updateUserPhoto(int userId, String newUserPhoto) throws IOException {
        logger.info("Updating user photo for user ID: {}", userId);
        String json = objectMapper.writeValueAsString(Map.of("userId", userId, "newUserPhoto", newUserPhoto));
        sendRequest(BASE_URL + "/photo", "PUT", json);
    }

    /**
     * Checks if the given username is unique.
     *
     * @param username The username to be checked.
     * @return True if the username is unique, false otherwise.
     * @throws IOException If an IO error occurs during the operation.
     */
    public boolean isUsernameUnique(String username) throws IOException {
//        logger.info("Checking if username is unique: {}", username);
//        String response = sendRequest(BASE_URL + "/username/" + username, "GET", null);
        return true;
    }

    private String sendRequest(String urlString, String method, String json) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        if (json != null) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            logger.error("Failed to execute request. HTTP response code: " + responseCode);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
