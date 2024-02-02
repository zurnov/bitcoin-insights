package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.domain.entity.Role;
import com.zurnov.bitcoin.insights.dto.RoleDTO;
import com.zurnov.bitcoin.insights.exception.OperationFailedException;
import com.zurnov.bitcoin.insights.exception.ResourceNotFoundException;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleService(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));
        return modelMapper.map(role, RoleDTO.class);
    }

    public RoleDTO createRole(RoleDTO roleDTO) {
        validateRole(roleDTO);

        try {
            Role role = modelMapper.map(roleDTO, Role.class);
            Role savedRole = roleRepository.save(role);
            return modelMapper.map(savedRole, RoleDTO.class);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to create role: " + e.getMessage());
        }
    }

    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    private void validateRole(RoleDTO roleDTO) {
        if (roleDTO.getRoleName() == null || roleDTO.getRoleName().isEmpty()) {
            throw new ValidationException("Role name is required.");
        }

        if (roleDTO.getDescription() == null || roleDTO.getDescription().isEmpty()) {
            throw new ValidationException("Role description is required.");
        }

        if (roleRepository.existsByRoleName(roleDTO.getRoleName())) {
            throw new ValidationException("Such role already exists " + roleDTO.getRoleName());
        }

    }

}
