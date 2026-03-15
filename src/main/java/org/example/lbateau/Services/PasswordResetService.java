package org.example.lbateau.Services;

import org.example.lbateau.Entity.User;
import org.example.lbateau.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private PasswordEncoder passwordEncoder;

    // Step 1 — find user by email or phone, generate code, send email
    public void sendResetCode(String emailOrPhone) {
        // Try email first, then phone
        Optional<User> userOpt = userRepository.findByEmail(emailOrPhone);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByTelephone(emailOrPhone);
        }

        // Always return success (don't reveal if email/phone exists)
        if (userOpt.isEmpty()) return;

        User user = userOpt.get();

        // Generate 6-digit code
        String code = String.format("%06d", new Random().nextInt(999999));

        // Expires in 15 minutes
        Date expiry = new Date(System.currentTimeMillis() + 15 * 60 * 1000);

        user.setResetCode(code);
        user.setResetCodeExpiry(expiry);
        userRepository.save(user);

        // Send email if email was provided
        if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(emailOrPhone)) {
            emailService.sendResetCode(user.getEmail(), code);
        }
    }

    // Step 2 — verify code and set new password
    public boolean resetPassword(String code, String newPassword) {
        Optional<User> userOpt = userRepository.findByResetCode(code);

        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();

        // Check if code is expired
        if (user.getResetCodeExpiry() == null || user.getResetCodeExpiry().before(new Date())) {
            return false;
        }

        // Set new password (hashed)
        user.setPassword(passwordEncoder.encode(newPassword));

        // Clear the reset code so it can't be reused
        user.setResetCode(null);
        user.setResetCodeExpiry(null);

        userRepository.save(user);
        //-
        return true;
    }
}