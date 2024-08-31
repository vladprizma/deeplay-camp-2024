package io.deeplay.camp.databaseservice.controller;

import io.deeplay.camp.databaseservice.dto.UserDTO;
import io.deeplay.camp.databaseservice.model.User;
import io.deeplay.camp.databaseservice.service.UserService;
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
        List<UserDTO> users = userService.getAllUsers();
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
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
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
        UserDTO user = userService.getUserByUsername(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
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
        userService.updateUsername(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
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
        userService.updateRating(id, rating);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Creates a new user.
     *
     * @param user The request object containing the details of the user to create.
     * @return A ResponseEntity containing the saved UserDTO object.
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        UserDTO savedUser = userService.saveUser(user);
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
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}