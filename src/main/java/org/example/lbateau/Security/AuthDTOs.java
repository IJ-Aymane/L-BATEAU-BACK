package org.example.lbateau.Security;

// ── Login request ──────────────────────────────────────────
// POST /api/auth/login  { "username": "...", "password": "..." }
public class AuthDTOs {

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // ── Login response ─────────────────────────────────────
    // Returns the JWT token
    public static class LoginResponse {
        private String token;

        public LoginResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }

    // ── Create user request (admin only) ───────────────────
    // POST /api/auth/users  { "username": "...", "password": "..." }
    public static class CreateUserRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}