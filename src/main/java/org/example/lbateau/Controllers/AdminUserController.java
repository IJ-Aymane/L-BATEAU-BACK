package org.example.lbateau.Controllers;

import org.example.lbateau.Entity.User;
import org.example.lbateau.Security.AuthDTOs;
import org.example.lbateau.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private AuthService authService;

    @GetMapping
    public List<AuthDTOs.UserResponse> getUsers() {
        return authService.listUsers();
    }

    @PostMapping
    public ResponseEntity<AuthDTOs.UserResponse> createClientUser(@RequestBody AuthDTOs.CreateUserRequest request) {
        User created = authService.createClientUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.toResponse(created));
    }

    @PutMapping("/{id}/password")
    public AuthDTOs.UserResponse updatePassword(@PathVariable String id,
                                                @RequestBody AuthDTOs.UpdatePasswordRequest request) {
        return authService.updatePassword(id, request);
    }
}
