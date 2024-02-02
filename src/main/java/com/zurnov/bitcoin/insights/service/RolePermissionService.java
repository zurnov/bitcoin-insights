package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.domain.entity.Permission;
import com.zurnov.bitcoin.insights.domain.entity.Role;
import com.zurnov.bitcoin.insights.repository.PermissionRepository;
import com.zurnov.bitcoin.insights.repository.RolePermissionRepository;
import com.zurnov.bitcoin.insights.domain.entity.RolePermission;
import com.zurnov.bitcoin.insights.dto.RolePermissionDTO;
import com.zurnov.bitcoin.insights.exception.OperationFailedException;
import com.zurnov.bitcoin.insights.exception.ResourceNotFoundException;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RolePermissionService(RolePermissionRepository rolePermissionRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    public List<RolePermissionDTO> getAllRolePermissions() {
        List<RolePermission> rolePermissions = rolePermissionRepository.findAll();
        return rolePermissions.stream()
                .map(rolePermission -> modelMapper.map(rolePermission, RolePermissionDTO.class)).toList();
    }

    public RolePermissionDTO getRolePermissionById(Long rolePermissionId) {
        RolePermission rolePermission = rolePermissionRepository.findById(rolePermissionId)
                .orElseThrow(() -> new ResourceNotFoundException("RolePermission not found with ID: " + rolePermissionId));
        return modelMapper.map(rolePermission, RolePermissionDTO.class);
    }

    public RolePermissionDTO createRolePermission(RolePermissionDTO rolePermissionDTO) {
        if (rolePermissionDTO.getRoleId() == null || rolePermissionDTO.getPermissionId() == null) {
            throw new ValidationException("Role and Permission are required.");
        }

        if (rolePermissionRepository.existsByRoleIdAndPermissionId(
                rolePermissionDTO.getRoleId(), rolePermissionDTO.getPermissionId())) {
            throw new ValidationException("RolePermission already exists for the specified Role and Permission.");
        }

        try {
            Role role = roleRepository.findById(rolePermissionDTO.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + rolePermissionDTO.getRoleId()));
            Permission permission = permissionRepository.findById(rolePermissionDTO.getPermissionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + rolePermissionDTO.getPermissionId()));

            RolePermission rolePermission = new RolePermission();
            rolePermission.setRole(role);
            rolePermission.setPermission(permission);

            RolePermission savedRolePermission = rolePermissionRepository.save(rolePermission);

            return modelMapper.map(savedRolePermission, RolePermissionDTO.class);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to create RolePermission: " + e.getMessage());
        }
    }

    public RolePermissionDTO updateRolePermission(Long rolePermissionId, RolePermissionDTO rolePermissionDTO) {

        RolePermission rolePermissionToUpdate = rolePermissionRepository.findById(rolePermissionId)
                .orElseThrow(() -> new ResourceNotFoundException("RolePermission not found with ID: " + rolePermissionId));

        modelMapper.map(rolePermissionDTO, rolePermissionToUpdate);

        try {
            RolePermission updatedRolePermission = rolePermissionRepository.save(rolePermissionToUpdate);
            return modelMapper.map(updatedRolePermission, RolePermissionDTO.class);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to update RolePermission: " + e.getMessage());
        }
    }

    public void deleteRolePermission(Long rolePermissionId) {

        if (!rolePermissionRepository.existsById(rolePermissionId)) {
            throw new ResourceNotFoundException("RolePermission not found with ID: " + rolePermissionId);
        }

        try {
            rolePermissionRepository.deleteById(rolePermissionId);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to delete RolePermission: " + e.getMessage());
        }
    }
}
