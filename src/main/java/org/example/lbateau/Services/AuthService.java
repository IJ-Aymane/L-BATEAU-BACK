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
import java.util.Date;
import java.util.LinkedHashSet;
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

    public User createUser(AuthDTOs.CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setTelephone(request.getTelephone());
        user.setRoles(normalizeRoles(request.getRoles()));
        user.setDateCreation(new Date());
        return userRepository.save(user);
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
}
