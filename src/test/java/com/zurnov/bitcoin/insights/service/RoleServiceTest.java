package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.domain.entity.Role;
import com.zurnov.bitcoin.insights.domain.entity.UserRole;
import com.zurnov.bitcoin.insights.dto.RoleDTO;
import com.zurnov.bitcoin.insights.exception.OperationFailedException;
import com.zurnov.bitcoin.insights.exception.ResourceNotFoundException;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.repository.RoleRepository;
import com.zurnov.bitcoin.insights.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoleService roleService;

    private Role role;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ROLE_USER");
        role.setDescription("A user role");

        roleDTO = new RoleDTO();
        roleDTO.setRoleId(1L);
        roleDTO.setRoleName("ROLE_USER");
        roleDTO.setDescription("A user role");
    }

    @Test
    void getAllRoles_ShouldReturnListOfRoleDTOs() {
        List<Role> roles = Arrays.asList(role);

        when(roleRepository.findAll()).thenReturn(roles);
        when(modelMapper.map(role, RoleDTO.class)).thenReturn(roleDTO);

        List<RoleDTO> result = roleService.getAllRoles();

        assertEquals(1, result.size());
        assertEquals(roleDTO, result.get(0));

        verify(roleRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(role, RoleDTO.class);
    }

    @Test
    void getRoleById_WhenRoleExists_ShouldReturnRoleDTO() {
        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));
        when(modelMapper.map(role, RoleDTO.class)).thenReturn(roleDTO);

        RoleDTO result = roleService.getRoleById(role.getRoleId());

        assertEquals(roleDTO, result);

        verify(roleRepository, times(1)).findById(role.getRoleId());
        verify(modelMapper, times(1)).map(role, RoleDTO.class);
    }

    @Test
    void getRoleById_WhenRoleDoesNotExist_ThrowsResourceNotFoundException() {
        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.getRoleById(role.getRoleId()));

        verify(roleRepository, times(1)).findById(role.getRoleId());
    }

    @Test
    void createRole_ShouldReturnCreatedRoleDTO() {
        when(roleRepository.existsByRoleName(roleDTO.getRoleName())).thenReturn(false);
        when(modelMapper.map(roleDTO, Role.class)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
        when(modelMapper.map(role, RoleDTO.class)).thenReturn(roleDTO);

        RoleDTO result = roleService.createRole(roleDTO);

        assertEquals(roleDTO, result);

        verify(roleRepository, times(1)).existsByRoleName(roleDTO.getRoleName());
        verify(roleRepository, times(1)).save(role);
        verify(modelMapper, times(1)).map(roleDTO, Role.class);
        verify(modelMapper, times(1)).map(role, RoleDTO.class);
    }

    @Test
    void createRole_WhenRoleNameExists_ThrowsValidationException() {
        when(roleRepository.existsByRoleName(roleDTO.getRoleName())).thenReturn(true);

        assertThrows(ValidationException.class, () -> roleService.createRole(roleDTO));

        verify(roleRepository, times(1)).existsByRoleName(roleDTO.getRoleName());
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void createRole_WhenValidationFails_ThrowsValidationException() {
        roleDTO.setRoleName(null);

        assertThrows(ValidationException.class, () -> roleService.createRole(roleDTO));

        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void createRole_WhenOperationFails_ThrowsOperationFailedException() {
        when(roleRepository.existsByRoleName(roleDTO.getRoleName())).thenReturn(false);
        when(modelMapper.map(roleDTO, Role.class)).thenReturn(role);
        when(roleRepository.save(role)).thenThrow(new RuntimeException("Database error"));

        assertThrows(OperationFailedException.class, () -> roleService.createRole(roleDTO));

        verify(roleRepository, times(1)).existsByRoleName(roleDTO.getRoleName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void deleteRole_WhenRoleExists_DeletesRoleAndAssociatedUserRoles() {
        List<UserRole> userRoles = Arrays.asList(new UserRole());

        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.of(role));
        when(userRoleRepository.findByRole(role)).thenReturn(userRoles);

        roleService.deleteRole(role.getRoleId());

        verify(roleRepository, times(1)).findById(role.getRoleId());
        verify(userRoleRepository, times(1)).findByRole(role);
        verify(userRoleRepository, times(1)).deleteAll(userRoles);
        verify(roleRepository, times(1)).deleteById(role.getRoleId());
    }

    @Test
    void deleteRole_WhenRoleDoesNotExist_ThrowsResourceNotFoundException() {
        when(roleRepository.findById(role.getRoleId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.deleteRole(role.getRoleId()));

        verify(roleRepository, times(1)).findById(role.getRoleId());
    }

    @Test
    void validateRole_WhenRoleNameIsNull_ThrowsValidationException() {
        roleDTO.setRoleName(null);

        assertThrows(ValidationException.class, () -> roleService.validateRole(roleDTO));
    }

    @Test
    void validateRole_WhenRoleNameIsEmpty_ThrowsValidationException() {
        roleDTO.setRoleName("");

        assertThrows(ValidationException.class, () -> roleService.validateRole(roleDTO));
    }

    @Test
    void validateRole_WhenDescriptionIsNull_ThrowsValidationException() {
        roleDTO.setDescription(null);

        assertThrows(ValidationException.class, () -> roleService.validateRole(roleDTO));
    }

    @Test
    void validateRole_WhenDescriptionIsEmpty_ThrowsValidationException() {
        roleDTO.setDescription("");

        assertThrows(ValidationException.class, () -> roleService.validateRole(roleDTO));
    }

    @Test
    void validateRole_WhenRoleAlreadyExists_ThrowsValidationException() {
        when(roleRepository.existsByRoleName(roleDTO.getRoleName())).thenReturn(true);

        assertThrows(ValidationException.class, () -> roleService.validateRole(roleDTO));
    }
}
