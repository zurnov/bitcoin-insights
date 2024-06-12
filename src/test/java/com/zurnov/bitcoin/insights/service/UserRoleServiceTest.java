package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.domain.entity.UserRole;
import com.zurnov.bitcoin.insights.domain.entity.UserRoleDetailed;
import com.zurnov.bitcoin.insights.dto.UserRoleDTO;
import com.zurnov.bitcoin.insights.dto.UserRoleDetailedDTO;
import com.zurnov.bitcoin.insights.exception.OperationFailedException;
import com.zurnov.bitcoin.insights.exception.ResourceNotFoundException;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserRoleService userRoleService;

    private UserRole userRole;
    private UserRoleDTO userRoleDTO;
    private UserRoleDetailed userRoleDetailed;
    private UserRoleDetailedDTO userRoleDetailedDTO;

    @BeforeEach
    void setUp() {
        userRole = new UserRole();
        userRole.setUserRoleId(1L);

        userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserRoleId(1L);

        userRoleDetailed = new UserRoleDetailed(1L, "testUser", "roleName", "description");

        userRoleDetailedDTO = new UserRoleDetailedDTO();
        userRoleDetailedDTO.setUserId(1L);
    }

    @Test
    void getAllUserRolesShouldReturnListOfUserRoles() {
        when(userRoleRepository.findAll()).thenReturn(List.of(userRole));
        when(modelMapper.map(userRole, UserRoleDTO.class)).thenReturn(userRoleDTO);

        List<UserRoleDTO> userRoleDTOList = userRoleService.getAllUserRoles();

        assertNotNull(userRoleDTOList);
        assertEquals(1, userRoleDTOList.size());
        assertEquals(userRoleDTO, userRoleDTOList.get(0));
    }

    @Test
    void getUserRoleByIdWhenUserRoleExistsShouldReturnUserRole() {
        when(userRoleRepository.findById(1L)).thenReturn(Optional.of(userRole));
        when(modelMapper.map(userRole, UserRoleDTO.class)).thenReturn(userRoleDTO);

        UserRoleDTO foundUserRole = userRoleService.getUserRoleById(1L);

        assertNotNull(foundUserRole);
        assertEquals(userRoleDTO, foundUserRole);
    }

    @Test
    void getUserRoleByIdWhenUserRoleDoesNotExistShouldThrowException() {
        when(userRoleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userRoleService.getUserRoleById(1L));
    }
    
    @Test
    void deleteUserRoleShouldDeleteUserRole() {
        doNothing().when(userRoleRepository).deleteById(1L);

        assertDoesNotThrow(() -> userRoleService.deleteUserRole(1L));
        verify(userRoleRepository, times(1)).deleteById(1L);
    }

    @Test
    void getUserDetailedRolesByIdShouldReturnListOfUserRoleDetailed() {
        when(userRoleRepository.getUserRolesDetailed(1L)).thenReturn(List.of(userRoleDetailed));
        when(modelMapper.map(userRoleDetailed, UserRoleDetailedDTO.class)).thenReturn(userRoleDetailedDTO);

        List<UserRoleDetailedDTO> userRoleDetailedDTOList = userRoleService.getUserDetailedRolesById(1L);

        assertNotNull(userRoleDetailedDTOList);
        assertEquals(1, userRoleDetailedDTOList.size());
        assertEquals(userRoleDetailedDTO, userRoleDetailedDTOList.get(0));
    }

    @Test
    void validateUserRoleWhenValidShouldReturnTrue() {
        userRoleDTO.setUserId(1L);
        userRoleDTO.setRoleId(1L);

        when(userRoleRepository.existsByUserIdAndRoleID(1L, 1L)).thenReturn(false);

        assertTrue(userRoleService.validateUserRole(userRoleDTO));
    }

    @Test
    void validateUserRoleWhenUserIdIsNullShouldThrowValidationException() {
        userRoleDTO.setUserId(null);

        assertThrows(ValidationException.class, () -> userRoleService.validateUserRole(userRoleDTO));
    }

    @Test
    void validateUserRoleWhenRoleIdIsNullShouldThrowValidationException() {
        userRoleDTO.setRoleId(null);

        assertThrows(ValidationException.class, () -> userRoleService.validateUserRole(userRoleDTO));
    }

    @Test
    void validateUserRoleWhenUserRoleExistsShouldThrowValidationException() {
        userRoleDTO.setUserId(1L);
        userRoleDTO.setRoleId(1L);

        when(userRoleRepository.existsByUserIdAndRoleID(1L, 1L)).thenReturn(true);

        assertThrows(ValidationException.class, () -> userRoleService.validateUserRole(userRoleDTO));
    }
}
