package org.example.lbateau.Security;

public class ResetDTOs {

    // POST /api/auth/forgot-password
    public static class ForgotRequest {
        private String emailOrPhone;
        public String getEmailOrPhone() { return emailOrPhone; }
        public void setEmailOrPhone(String v) { this.emailOrPhone = v; }
    }

    // POST /api/auth/reset-password
    public static class ResetRequest {
        private String code;
        private String newPassword;
        public String getCode() { return code; }
        public void setCode(String v) { this.code = v; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String v) { this.newPassword = v; }
    }

    // Generic response
    public static class MessageResponse {
        private String message;
        public MessageResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}