package io.deeplay.camp.databaseservice.controller;

import io.deeplay.camp.databaseservice.dto.UserDTO;
import io.deeplay.camp.databaseservice.model.User;
import io.deeplay.camp.databaseservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    /**
     * Constructs a new UserController with the specified user service.
     *
     * @param userService The service for managing users.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     *
     * @return A ResponseEntity containing a list of UserDTO objects.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Retrieving all users");
        List<UserDTO> users = userService.getAllUsers();
        logger.info("Retrieved {} users", users.size());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A ResponseEntity containing the UserDTO object, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        logger.info("Retrieving user by ID: {}", id);
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            logger.info("User found with ID: {}", id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            logger.warn("User not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return A ResponseEntity containing the UserDTO object, or a 200 status with null if not found.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        logger.info("Retrieving user by username: {}", username);
        UserDTO user = userService.getUserByUsername(username);
        if (user != null) {
            logger.info("User found with username: {}", username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            logger.warn("User not found with username: {}", username);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    /**
     * Updates the username of a user.
     *
     * @param id The ID of the user to update.
     * @param username The new username to set.
     * @return A ResponseEntity with an OK status.
     */
    @PutMapping("/{id}/username")
    public ResponseEntity<Void> updateUsername(@PathVariable int id, @RequestParam String username) {
        logger.info("Updating username for user ID: {}", id);
        try {
            userService.updateUsername(id, username);
            logger.info("Updated username for user ID: {}", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("User not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the rating of a user.
     *
     * @param id The ID of the user to update.
     * @param rating The new rating to set.
     * @return A ResponseEntity with an OK status.
     */
    @PutMapping("/{id}/rating")
    public ResponseEntity<Void> updateRating(@PathVariable int id, @RequestParam int rating) {
        logger.info("Updating rating for user ID: {}", id);
        try {
            userService.updateRating(id, rating);
            logger.info("Updated rating for user ID: {}", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("User not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new user.
     *
     * @param user The request object containing the details of the user to create.
     * @return A ResponseEntity containing the saved UserDTO object.
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        logger.info("Creating new user");
        UserDTO savedUser = userService.saveUser(user);
        logger.info("Created new user with ID: {}", savedUser.getId());
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id The ID of the user to delete.
     * @return A ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        logger.info("Deleting user with ID: {}", id);
        try {
            userService.deleteUser(id);
            logger.info("Deleted user with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            logger.warn("User not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}