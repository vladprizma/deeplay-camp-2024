package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.dto.DTOMapper;
import io.deeplay.camp.databaseservice.dto.UserDTO;
import io.deeplay.camp.databaseservice.model.User;
import io.deeplay.camp.databaseservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * Repository for accessing users.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a new UserService with the specified repository.
     *
     * @param userRepository Repository for accessing users.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users.
     *
     * @return A list of UserDTO objects representing all users.
     */
    public List<UserDTO> getAllUsers() {
        logger.info("Retrieving all users");
        List<UserDTO> users = userRepository.findAll().stream()
                .map(DTOMapper::toUserDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} users", users.size());
        return users;
    }

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return A UserDTO object representing the user, or null if not found.
     */
    public UserDTO getUserById(int id) {
        logger.info("Retrieving user by ID: {}", id);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            logger.info("User found with ID: {}", id);
            return DTOMapper.toUserDTO(userOptional.get());
        } else {
            logger.warn("User not found with ID: {}", id);
            return null;
        }
    }

    /**
     * Retrieves a user by its username.
     *
     * @param username The username of the user.
     * @return A UserDTO object representing the user, or null if not found.
     */
    public UserDTO getUserByUsername(String username) {
        logger.info("Retrieving user by username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            logger.info("User found with username: {}", username);
            return DTOMapper.toUserDTO(user);
        } else {
            logger.warn("User not found with username: {}", username);
            return null;
        }
    }

    /**
     * Updates the username of a user.
     *
     * @param userId The unique identifier of the user.
     * @param username The new username of the user.
     */
    public void updateUsername(int userId, String username) {
        logger.info("Updating username for user ID: {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(username);
            userRepository.save(user);
            logger.info("Updated username for user ID: {}", userId);
        } else {
            logger.warn("User not found with ID: {}", userId);
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }

    /**
     * Updates the rating of a user.
     *
     * @param userId The unique identifier of the user.
     * @param rating The new rating to be added to the user's current rating.
     */
    public void updateRating(int userId, int rating) {
        logger.info("Updating rating for user ID: {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRating(user.getRating() + rating);
            userRepository.save(user);
            logger.info("Updated rating for user ID: {}", userId);
        } else {
            logger.warn("User not found with ID: {}", userId);
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }

    /**
     * Saves a new user or updates an existing one.
     *
     * @param user The User object containing the details of the user to be saved.
     * @return A UserDTO object representing the saved user.
     */
    public UserDTO saveUser(User user) {
        logger.info("Saving user with ID: {}", user.getId());
        User savedUser = userRepository.save(user);
        logger.info("Saved user with ID: {}", savedUser.getId());
        return DTOMapper.toUserDTO(savedUser);
    }

    /**
     * Deletes a user by its unique identifier.
     *
     * @param id The unique identifier of the user to be deleted.
     */
    public void deleteUser(int id) {
        logger.info("Deleting user with ID: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("Deleted user with ID: {}", id);
        } else {
            logger.warn("User not found with ID: {}", id);
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
    }
}