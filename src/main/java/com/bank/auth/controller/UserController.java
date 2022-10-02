package com.bank.auth.controller;

import com.bank.auth.dto.LoginRequestDTO;
import com.bank.auth.dto.LoginResponseDTO;
import com.bank.auth.dto.UserRequestDTO;
import com.bank.auth.dto.UserResponseDTO;
import com.bank.auth.service.UserService;
import com.bank.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@RequestBody UserRequestDTO request) {
        UserResponseDTO user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(user, "User created successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO request) {
        UserResponseDTO user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(user, "User updated successfully"));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> addRoleToUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        userService.addRoleToUser(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success(null, "Role added successfully"));
    }
}
