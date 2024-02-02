package com.zurnov.bitcoin.insights.controller;

import com.zurnov.bitcoin.insights.dto.PermissionDTO;
import com.zurnov.bitcoin.insights.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        List<PermissionDTO> permissions = permissionService.getAllPermissions();
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long permissionId) {
        PermissionDTO permission = permissionService.getPermissionById(permissionId);
        return new ResponseEntity<>(permission, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
        PermissionDTO createdPermission = permissionService.createPermission(permissionDTO);
        return new ResponseEntity<>(createdPermission, HttpStatus.CREATED);
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable Long permissionId, @RequestBody PermissionDTO permissionDTO) {
        PermissionDTO updatedPermission = permissionService.updatePermission(permissionId, permissionDTO);
        return new ResponseEntity<>(updatedPermission, HttpStatus.OK);
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long permissionId) {
        permissionService.deletePermission(permissionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
