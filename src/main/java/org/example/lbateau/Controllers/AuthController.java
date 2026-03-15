package org.example.lbateau.Controllers;

import org.example.lbateau.Entity.User;
import org.example.lbateau.Security.AuthDTOs;
import org.example.lbateau.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * POST /api/auth/login
     * Public endpoint – no token required.
     * Body: { "username": "admin", "password": "secret" }
     * Returns: { "token": "<jwt>" }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthDTOs.LoginResponse> login(@RequestBody AuthDTOs.LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthDTOs.LoginResponse(token));
    }

    /**
     * POST /api/auth/users
     * Protected – requires a valid JWT in the Authorization header.
     * Only logged-in users (admins) can call this.
     * Body: { "username": "newuser", "password": "password123" }
     */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody AuthDTOs.CreateUserRequest request) {
        User created = authService.createUser(request);
        created.setPassword(null); // Never return the hashed password
        return ResponseEntity.ok(created);
    }
}