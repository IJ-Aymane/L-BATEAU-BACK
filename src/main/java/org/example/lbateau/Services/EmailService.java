package org.example.lbateau.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendResetCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("L'BATEAU — Code de réinitialisation");
        message.setText(
                "Bonjour,\n\n" +
                        "Votre code de réinitialisation de mot de passe est :\n\n" +
                        "  " + code + "\n\n" +
                        "Ce code expire dans 15 minutes.\n\n" +
                        "Si vous n'avez pas demandé ce code, ignorez cet email.\n\n" +
                        "— L'Bateau Management System"
        );
        mailSender.send(message);
    }
}