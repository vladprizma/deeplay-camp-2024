package io.deeplay.camp.databaseservice.service;

import io.deeplay.camp.databaseservice.dto.DTOMapper;
import io.deeplay.camp.databaseservice.dto.UserDTO;
import io.deeplay.camp.databaseservice.model.User;
import io.deeplay.camp.databaseservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(DTOMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(int id) {
        return DTOMapper.toUserDTO(userRepository.findById(id).orElse(null));
    }

    public UserDTO getUserByUsername(String username) {
        return DTOMapper.toUserDTO(userRepository.findByUsername(username));
    }

    public void updateUsername(int userId, String username) {
        var user = userRepository.findById(userId).get();
        user.setUsername(username);
        userRepository.save(user);
    }

    public void updateRating(int userId, int rating) {
        var user = userRepository.findById(userId).get();
        user.setRating(user.getRating() + rating);
        userRepository.save(user);
    }
    
    public UserDTO saveUser(User user) {
        var savedUser = userRepository.save(user);
        return  DTOMapper.toUserDTO(savedUser);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
