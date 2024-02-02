package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.domain.entity.UserRole;
import com.zurnov.bitcoin.insights.dto.UserRoleDTO;
import com.zurnov.bitcoin.insights.exception.OperationFailedException;
import com.zurnov.bitcoin.insights.exception.ResourceNotFoundException;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.repository.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository, ModelMapper modelMapper) {
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
    }

    public List<UserRoleDTO> getAllUserRoles() {
        List<UserRole> userRoles = userRoleRepository.findAll();
        return userRoles.stream()
                .map(userRole -> modelMapper.map(userRole, UserRoleDTO.class))
                .collect(Collectors.toList());
    }

    public UserRoleDTO getUserRoleById(Long userRoleId) {
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new ResourceNotFoundException("User Role not found with ID: " + userRoleId));
        return modelMapper.map(userRole, UserRoleDTO.class);
    }

    public UserRoleDTO createUserRole(UserRoleDTO userRoleDTO) {
        validateUserRole(userRoleDTO);

        try {
            UserRole userRole = modelMapper.map(userRoleDTO, UserRole.class);
            UserRole savedUserRole = userRoleRepository.save(userRole);
            return modelMapper.map(savedUserRole, UserRoleDTO.class);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to create user role: " + e.getMessage());
        }
    }

    public void deleteUserRole(Long userRoleId) {
        userRoleRepository.deleteById(userRoleId);
    }

    public boolean validateUserRole(UserRoleDTO userRoleDTO) {

        if (userRoleDTO.getUserId() == null || userRoleDTO.getRoleId() == null) {
            throw new ValidationException("User ID and Role ID are required.");
        }

        if (userRoleRepository.existsByUserIdAndRoleID(userRoleDTO.getRoleId(), userRoleDTO.getUserId())) {
            throw new ValidationException("Such UserRole already exists.");
        }

        return true;
    }

}
