package com.zurnov.bitcoin.insights.controller;

import com.zurnov.bitcoin.insights.dto.UserRoleDTO;
import com.zurnov.bitcoin.insights.service.UserRoleService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-roles")
@Hidden
public class UserRoleController {

    private final UserRoleService userRoleService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> getAllUserRoles() {
        List<UserRoleDTO> userRoles = userRoleService.getAllUserRoles();
        return new ResponseEntity<>(userRoles, HttpStatus.OK);
    }

    @GetMapping("/{userRoleId}")
    public ResponseEntity<UserRoleDTO> getUserRoleById(@PathVariable Long userRoleId) {
        UserRoleDTO userRole = userRoleService.getUserRoleById(userRoleId);
        return new ResponseEntity<>(userRole, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<UserRoleDTO> createUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        UserRoleDTO createdUserRole = userRoleService.createUserRole(userRoleDTO);
        return new ResponseEntity<>(createdUserRole, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userRoleId}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long userRoleId) {
        userRoleService.deleteUserRole(userRoleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
