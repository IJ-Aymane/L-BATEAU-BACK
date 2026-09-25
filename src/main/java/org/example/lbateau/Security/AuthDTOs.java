package org.example.lbateau.Security;

import org.example.lbateau.Entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        private String username;
        private List<String> roles = new ArrayList<>();

        public LoginResponse(String token, String username, List<String> roles) {
            this.token = token;
            this.username = username;
            this.roles = roles != null ? roles : new ArrayList<>();
        }

        public String getToken() { return token; }
        public String getUsername() { return username; }
        public List<String> getRoles() { return roles; }
    }

    public static class UserResponse {
        private String id;
        private String username;
        private String email;
        private String telephone;
        private List<String> roles = new ArrayList<>();
        private Date dateCreation;

        public UserResponse(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.telephone = user.getTelephone();
            this.roles = new ArrayList<>(user.getRoles());
            this.dateCreation = user.getDateCreation();
        }

        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getTelephone() { return telephone; }
        public List<String> getRoles() { return roles; }
        public Date getDateCreation() { return dateCreation; }
    }

    public static class CreateUserRequest {
        private String username;
        private String password;
        private String email;
        private String telephone;
        private List<String> roles = new ArrayList<>();
        public String getUsername()  { return username; }
        public void setUsername(String v)  { this.username = v; }
        public String getPassword()  { return password; }
        public void setPassword(String v)  { this.password = v; }
        public String getEmail()     { return email; }
        public void setEmail(String v)     { this.email = v; }
        public String getTelephone() { return telephone; }
        public void setTelephone(String v) { this.telephone = v; }
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles != null ? roles : new ArrayList<>(); }
    }

    public static class UpdatePasswordRequest {
        private String password;
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class UpdateRolesRequest {
        private List<String> roles = new ArrayList<>();
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles != null ? roles : new ArrayList<>(); }
    }
}
