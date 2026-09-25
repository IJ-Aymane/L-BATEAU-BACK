package org.example.lbateau.Services;

import org.example.lbateau.Entity.User;
import org.example.lbateau.Repository.UserRepository;
import org.example.lbateau.Security.AuthDTOs;
import org.example.lbateau.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public AuthDTOs.LoginResponse login(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        Set<String> roles = normalizeRoles(user.getRoles());
        String token = jwtUtil.generateToken(user.getUsername(), roles);
        return new AuthDTOs.LoginResponse(token, user.getUsername(), new ArrayList<>(roles));
    }

    public List<AuthDTOs.UserResponse> listUsers() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getDateCreation, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toResponse)
                .toList();
    }

    public User createClientUser(AuthDTOs.CreateUserRequest request) {
        request.setRoles(List.of("ROLE_CLIENT"));
        return createUser(request);
    }

    public User createUser(AuthDTOs.CreateUserRequest request) {
        validateCreateUser(request);
        if (userRepository.existsByUsername(request.getUsername().trim())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(blankToNull(request.getEmail()));
        user.setTelephone(blankToNull(request.getTelephone()));
        user.setRoles(normalizeRoles(request.getRoles()));
        user.setDateCreation(new Date());
        return userRepository.save(user);
    }

    public AuthDTOs.UserResponse updatePassword(String id, AuthDTOs.UpdatePasswordRequest request) {
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must contain at least 6 characters");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Set<String> roles = normalizeRoles(user.getRoles());
        if (!(roles.size() == 1 && roles.contains("ROLE_CLIENT"))) {
            throw new IllegalArgumentException("Only client account passwords can be changed from this screen");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return toResponse(userRepository.save(user));
    }

    public AuthDTOs.UserResponse toResponse(User user) {
        return new AuthDTOs.UserResponse(user);
    }

    private void validateCreateUser(AuthDTOs.CreateUserRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must contain at least 6 characters");
        }
    }

    private Set<String> normalizeRoles(Collection<String> roles) {
        Set<String> normalized = new LinkedHashSet<>();
        if (roles != null) {
            roles.stream()
                    .map(AuthService::normalizeRole)
                    .filter(role -> !role.isBlank())
                    .forEach(normalized::add);
        }
        if (normalized.isEmpty()) {
            normalized.add("ROLE_CLIENT");
        }
        return normalized;
    }

    private static String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "";
        }
        String value = role.trim().toUpperCase();
        return value.startsWith("ROLE_") ? value : "ROLE_" + value;
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
