package org.example.lbateau.Security;

public class AuthDTOs {

    public static class LoginRequest {
        private String username;
        private String password;
        public String getUsername() { return username; }
        public void setUsername(String v) { this.username = v; }
        public String getPassword() { return password; }
        public void setPassword(String v) { this.password = v; }
    }

    public static class LoginResponse {
        private String token;
        public LoginResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }

    public static class CreateUserRequest {
        private String username;
        private String password;
        private String email;
        private String telephone;
        public String getUsername()  { return username; }
        public void setUsername(String v)  { this.username = v; }
        public String getPassword()  { return password; }
        public void setPassword(String v)  { this.password = v; }
        public String getEmail()     { return email; }
        public void setEmail(String v)     { this.email = v; }
        public String getTelephone() { return telephone; }
        public void setTelephone(String v) { this.telephone = v; }
    }
}