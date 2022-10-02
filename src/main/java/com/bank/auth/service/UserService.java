package com.bank.auth.service;

import com.bank.auth.dto.LoginRequestDTO;
import com.bank.auth.dto.LoginResponseDTO;
import com.bank.auth.dto.UserRequestDTO;
import com.bank.auth.dto.UserResponseDTO;
import com.bank.auth.entity.Role;
import com.bank.auth.entity.User;
import com.bank.auth.repository.RoleRepository;
import com.bank.auth.repository.UserRepository;
import com.bank.auth.security.JwtTokenProvider;
import com.bank.common.exception.BusinessException;
import com.bank.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

            String token = jwtTokenProvider.generateToken(user.getUsername(), roles);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

            return LoginResponseDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(86400000L) // 24 hours
                .username(user.getUsername())
                .build();
        } catch (Exception e) {
            throw new BusinessException("LOGIN_FAILED", "Invalid username or password");
        }
    }

    public UserResponseDTO createUser(UserRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("USER_EXISTS", "Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("EMAIL_EXISTS", "Email already exists");
        }

        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .roles(new HashSet<>())
            .phone(request.getPhone())
            .active(true)
            .build();

        // Add USER role by default
        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new ResourceNotFoundException("ROLE_USER not found"));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToDTO(user);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public void addRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.getRoles().add(role);
        userRepository.save(user);
    }

    public void removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.getRoles().remove(role);
        userRepository.save(user);
    }

    private UserResponseDTO mapToDTO(User user) {
        return UserResponseDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .phone(user.getPhone())
            .active(user.getActive())
            .roles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()))
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}
