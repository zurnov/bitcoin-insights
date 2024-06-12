package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.dto.UserDTO;
import com.zurnov.bitcoin.insights.dto.UserRoleDTO;
import com.zurnov.bitcoin.insights.exception.OperationFailedException;
import com.zurnov.bitcoin.insights.exception.ResourceNotFoundException;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.repository.UserRepository;
import com.zurnov.bitcoin.insights.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
    }

    @Test
    void getAllUsersShouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        List<UserDTO> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(userDTO, users.get(0));
    }

    @Test
    void getUserByIdWhenUserExistsShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(userDTO, foundUser);
    }

    @Test
    void getUserByIdWhenUserDoesNotExistShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void createUserWhenValidShouldReturnCreatedUser() {
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(userDTO, createdUser);
        verify(userRoleService, times(1)).createUserRole(any(UserRoleDTO.class));
    }

    @Test
    void createUserWhenUsernameIsEmptyShouldThrowValidationException() {
        userDTO.setUsername("");

        assertThrows(ValidationException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void createUserWhenExceptionThrownShouldThrowOperationFailedException() {
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(OperationFailedException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void updateUserWhenUserExistsShouldReturnUpdatedUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        doReturn(userDTO).when(modelMapper).map(user, UserDTO.class);
        doNothing().when(modelMapper).map(userDTO, user);

        UserDTO updatedUser = userService.updateUser(1L, userDTO);

        assertNotNull(updatedUser);
        assertEquals(userDTO, updatedUser);
    }

    @Test
    void updateUserWhenUserDoesNotExistShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, userDTO));
    }

    @Test
    void updateUserWhenExceptionThrownShouldThrowOperationFailedException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(modelMapper).map(userDTO, user);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(OperationFailedException.class, () -> userService.updateUser(1L, userDTO));
    }

    @Test
    void deleteUserWhenUserExistsShouldNotThrowException() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUserWhenUserDoesNotExistShouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void deleteUserWhenExceptionThrownShouldThrowOperationFailedException() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(userRepository).deleteById(1L);

        assertThrows(OperationFailedException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void existsByUsernameShouldReturnTrueWhenUserExists() {
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        boolean exists = userService.existsByUsername("testUser");

        assertTrue(exists);
    }

    @Test
    void existsByEmailShouldReturnTrueWhenUserExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean exists = userService.existsByEmail("test@example.com");

        assertTrue(exists);
    }

    @Test
    void getUsersByUsernameShouldReturnListOfUsers() {
        List<User> users = List.of(user);
        when(userRepository.findAllByUsernameContaining("testUser")).thenReturn(users);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        List<UserDTO> userDTOList = userService.getUsersByUsername("testUser");

        assertNotNull(userDTOList);
        assertEquals(1, userDTOList.size());
        assertEquals(userDTO, userDTOList.get(0));
    }

    @Test
    void getUsersByIdAndUsernameShouldReturnListOfUsers() {
        List<User> users = List.of(user);
        when(userRepository.findAllByUserIdAndUsernameContaining(1L, "testUser")).thenReturn(users);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        List<UserDTO> userDTOList = userService.getUsersByIdAndUsername(1L, "testUser");

        assertNotNull(userDTOList);
        assertEquals(1, userDTOList.size());
        assertEquals(userDTO, userDTOList.get(0));
    }
}
