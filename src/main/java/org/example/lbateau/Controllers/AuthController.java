package org.example.lbateau.Controllers;

import org.example.lbateau.Entity.User;
import org.example.lbateau.Security.AuthDTOs;
import org.example.lbateau.Security.ResetDTOs;
import org.example.lbateau.Services.AuthService;
import org.example.lbateau.Services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private PasswordResetService passwordResetService;

    // ── Login ──────────────────────────────────────────────
    // POST /api/auth/login  { "username": "...", "password": "..." }
    @PostMapping("/login")
    public ResponseEntity<AuthDTOs.LoginResponse> login(@RequestBody AuthDTOs.LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDTOs.LoginResponse> register(@RequestBody AuthDTOs.CreateUserRequest request) {
        return ResponseEntity.ok(authService.registerClient(request));
    }


    //---bhb
    // ── Create user (protected) ────────────────────────────
    // POST /api/auth/users  { "username": "...", "password": "...", "email": "..." }
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody AuthDTOs.CreateUserRequest request) {
        User created = authService.createClientUser(request);
        created.setPassword(null);
        return ResponseEntity.ok(created);
    }

    // ── Forgot password ────────────────────────────────────
    // POST /api/auth/forgot-password  { "emailOrPhone": "admin@gmail.com" }
    // PUBLIC — no token required
    @PostMapping("/forgot-password")
    public ResponseEntity<ResetDTOs.MessageResponse> forgotPassword(
            @RequestBody ResetDTOs.ForgotRequest request) {
        passwordResetService.sendResetCode(request.getEmailOrPhone());
        // Always return same message (security: don't reveal if email exists)
        return ResponseEntity.ok(new ResetDTOs.MessageResponse(
                "Si cet email/téléphone existe, un code a été envoyé."
        ));
    }

    // ── Reset password ─────────────────────────────────────
    // POST /api/auth/reset-password  { "code": "123456", "newPassword": "..." }
    // PUBLIC — no token required
    @PostMapping("/reset-password")
    public ResponseEntity<ResetDTOs.MessageResponse> resetPassword(
            @RequestBody ResetDTOs.ResetRequest request) {
        boolean success = passwordResetService.resetPassword(
                request.getCode(), request.getNewPassword()
        );
        if (success) {
            return ResponseEntity.ok(new ResetDTOs.MessageResponse("Mot de passe changé avec succès."));
        }
        return ResponseEntity.badRequest().body(
                new ResetDTOs.MessageResponse("Code invalide ou expiré.")
        );
    }
}