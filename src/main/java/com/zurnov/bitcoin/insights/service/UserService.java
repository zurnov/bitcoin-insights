package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.repository.UserRepository;
import com.zurnov.bitcoin.insights.domain.entity.User;
import com.zurnov.bitcoin.insights.dto.UserDTO;
import com.zurnov.bitcoin.insights.exception.OperationFailedException;
import com.zurnov.bitcoin.insights.exception.ResourceNotFoundException;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class)).toList();
    }

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return modelMapper.map(user, UserDTO.class);
    }

    public List<UserDTO> getUsersByUsername(String username) {
        List<User> users = userRepository.findAllByUsernameContaining(username);
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class)).toList();
    }

    public List<UserDTO> getUsersByIdAndUsername(Long userId, String username) {
        List<User> users = userRepository.findAllByUserIdAndUsernameContaining(userId, username);
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class)).toList();
    }

    public UserDTO createUser(UserDTO userDTO) {

        // TODO Create a separate validation class PreProcessor
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            throw new ValidationException("Username is required.");
        }

        try {
            User user = modelMapper.map(userDTO, User.class);
            User savedUser = userRepository.save(user);
            return modelMapper.map(savedUser, UserDTO.class);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to create user: " + e.getMessage());
        }
    }

    public UserDTO updateUser(Long userId, UserDTO userDTO) {

        //TODO Validation
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        modelMapper.map(userDTO, userToUpdate);

        try {
            User updatedUser = userRepository.save(userToUpdate);
            return modelMapper.map(updatedUser, UserDTO.class);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to update user: " + e.getMessage());
        }
    }

    public void deleteUser(Long userId) {

        //TODO VALIDATION
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to delete user: " + e.getMessage());
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

