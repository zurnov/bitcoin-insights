package com.zurnov.bitcoin.insights.controller;

import com.zurnov.bitcoin.insights.dto.RolePermissionDTO;
import com.zurnov.bitcoin.insights.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rolePermissions")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @Autowired
    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping
    public ResponseEntity<List<RolePermissionDTO>> getAllRolePermissions() {
        List<RolePermissionDTO> rolePermissions = rolePermissionService.getAllRolePermissions();
        return new ResponseEntity<>(rolePermissions, HttpStatus.OK);
    }

    @GetMapping("/{rolePermissionId}")
    public ResponseEntity<RolePermissionDTO> getRolePermissionById(@PathVariable Long rolePermissionId) {
        RolePermissionDTO rolePermission = rolePermissionService.getRolePermissionById(rolePermissionId);
        return new ResponseEntity<>(rolePermission, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RolePermissionDTO> createRolePermission(@RequestBody RolePermissionDTO rolePermissionDTO) {
        RolePermissionDTO createdRolePermission = rolePermissionService.createRolePermission(rolePermissionDTO);
        return new ResponseEntity<>(createdRolePermission, HttpStatus.CREATED);
    }

    @PutMapping("/{rolePermissionId}")
    public ResponseEntity<RolePermissionDTO> updateRolePermission(@PathVariable Long rolePermissionId, @RequestBody RolePermissionDTO rolePermissionDTO) {
        RolePermissionDTO updatedRolePermission = rolePermissionService.updateRolePermission(rolePermissionId, rolePermissionDTO);
        return new ResponseEntity<>(updatedRolePermission, HttpStatus.OK);
    }

    @DeleteMapping("/{rolePermissionId}")
    public ResponseEntity<Void> deleteRolePermission(@PathVariable Long rolePermissionId) {
        rolePermissionService.deleteRolePermission(rolePermissionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
