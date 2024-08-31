package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.dto.DTOMapper;
import io.deeplay.camp.databaseservice.dto.UserDTO;
import io.deeplay.camp.databaseservice.model.User;
import io.deeplay.camp.databaseservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

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
        return userRepository.findAll().stream()
                .map(DTOMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return A UserDTO object representing the user, or null if not found.
     */
    public UserDTO getUserById(int id) {
        return DTOMapper.toUserDTO(userRepository.findById(id).orElse(null));
    }

    /**
     * Retrieves a user by its username.
     *
     * @param username The username of the user.
     * @return A UserDTO object representing the user, or null if not found.
     */
    public UserDTO getUserByUsername(String username) {
        return DTOMapper.toUserDTO(userRepository.findByUsername(username));
    }

    /**
     * Updates the username of a user.
     *
     * @param userId The unique identifier of the user.
     * @param username The new username of the user.
     */
    public void updateUsername(int userId, String username) {
        var user = userRepository.findById(userId).get();
        user.setUsername(username);
        userRepository.save(user);
    }

    /**
     * Updates the rating of a user.
     *
     * @param userId The unique identifier of the user.
     * @param rating The new rating to be added to the user's current rating.
     */
    public void updateRating(int userId, int rating) {
        var user = userRepository.findById(userId).get();
        user.setRating(user.getRating() + rating);
        userRepository.save(user);
    }

    /**
     * Saves a new user or updates an existing one.
     *
     * @param user The User object containing the details of the user to be saved.
     * @return A UserDTO object representing the saved user.
     */
    public UserDTO saveUser(User user) {
        var savedUser = userRepository.save(user);
        return DTOMapper.toUserDTO(savedUser);
    }

    /**
     * Deletes a user by its unique identifier.
     *
     * @param id The unique identifier of the user to be deleted.
     */
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}