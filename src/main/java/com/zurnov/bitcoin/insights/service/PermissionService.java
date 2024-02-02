package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.repository.PermissionRepository;
import com.zurnov.bitcoin.insights.domain.entity.Permission;
import com.zurnov.bitcoin.insights.dto.PermissionDTO;
import com.zurnov.bitcoin.insights.exception.OperationFailedException;
import com.zurnov.bitcoin.insights.exception.ResourceNotFoundException;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    public List<PermissionDTO> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(permission -> modelMapper.map(permission, PermissionDTO.class)).toList();
    }

    public PermissionDTO getPermissionById(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + permissionId));
        return modelMapper.map(permission, PermissionDTO.class);
    }

    public PermissionDTO createPermission(PermissionDTO permissionDTO) {

        // TODO Create a separate validation class PreProcessor
        if (permissionDTO.getPermissionName() == null || permissionDTO.getPermissionName().isEmpty()) {
            throw new ValidationException("Permission name is required.");
        }

        try {
            Permission permission = modelMapper.map(permissionDTO, Permission.class);
            Permission savedPermission = permissionRepository.save(permission);
            return modelMapper.map(savedPermission, PermissionDTO.class);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to create permission: " + e.getMessage());
        }
    }

    public PermissionDTO updatePermission(Long permissionId, PermissionDTO permissionDTO) {

        Permission permissionToUpdate = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + permissionId));

        modelMapper.map(permissionDTO, permissionToUpdate);

        try {
            Permission updatedPermission = permissionRepository.save(permissionToUpdate);
            return modelMapper.map(updatedPermission, PermissionDTO.class);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to update permission: " + e.getMessage());
        }
    }

    public void deletePermission(Long permissionId) {

        if (!permissionRepository.existsById(permissionId)) {
            throw new ResourceNotFoundException("Permission not found with ID: " + permissionId);
        }

    }
}