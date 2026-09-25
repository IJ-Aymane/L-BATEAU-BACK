package org.example.lbateau.Config;

import org.example.lbateau.Entity.Bateau;
import org.example.lbateau.Entity.Client;
import org.example.lbateau.Entity.Contact;
import org.example.lbateau.Entity.Reservation;
import org.example.lbateau.Entity.User;
import org.example.lbateau.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Configuration
public class DatabaseInitializer {

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    @Value("${app.seed.admin-password:admin123}")
    private String adminPassword;

    @Value("${app.seed.manager-password:manager123}")
    private String managerPassword;

    @Value("${app.seed.client-password:client123}")
    private String clientPassword;

    @Bean
    CommandLineRunner initializeDatabase(MongoTemplate mongoTemplate,
                                         UserRepository userRepository,
                                         PasswordEncoder passwordEncoder) {
        return args -> {
            ensureCollection(mongoTemplate, User.class);
            ensureCollection(mongoTemplate, Bateau.class);
            ensureCollection(mongoTemplate, Client.class);
            ensureCollection(mongoTemplate, Reservation.class);
            ensureCollection(mongoTemplate, Contact.class);

            boolean usersWereEmpty = userRepository.count() == 0;
            repairExistingUsers(userRepository, passwordEncoder);

            if (seedEnabled && usersWereEmpty) {
                userRepository.save(seedUser(
                        "admin",
                        adminPassword,
                        "admin@lbateau.local",
                        "+212600000001",
                        Set.of("ROLE_ADMIN"),
                        passwordEncoder
                ));
                userRepository.save(seedUser(
                        "manager",
                        managerPassword,
                        "manager@lbateau.local",
                        "+212600000002",
                        Set.of("ROLE_MANAGER"),
                        passwordEncoder
                ));
                userRepository.save(seedUser(
                        "client",
                        clientPassword,
                        "client@lbateau.local",
                        "+212600000003",
                        Set.of("ROLE_CLIENT"),
                        passwordEncoder
                ));
            }
        };
    }

    private void ensureCollection(MongoTemplate mongoTemplate, Class<?> entityClass) {
        if (!mongoTemplate.collectionExists(entityClass)) {
            mongoTemplate.createCollection(entityClass);
        }
    }

    private void repairExistingUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        userRepository.findAll().forEach(user -> {
            boolean changed = false;

            if (user.getUsername() == null || user.getUsername().isBlank()) {
                user.setUsername("user-" + Objects.toString(user.getId(), String.valueOf(System.currentTimeMillis())));
                changed = true;
            }
            if (user.getDateCreation() == null) {
                user.setDateCreation(new Date());
                changed = true;
            }
            Set<String> normalizedRoles = normalizeRoles(user.getRoles(), "ROLE_CLIENT");
            if (!normalizedRoles.equals(user.getRoles())) {
                user.setRoles(normalizedRoles);
                changed = true;
            }
            if (user.getPassword() == null || user.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode("changeme123"));
                changed = true;
            } else if (!isBcryptHash(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                changed = true;
            }

            if (changed) {
                userRepository.save(user);
            }
        });
    }

    private User seedUser(String username,
                          String rawPassword,
                          String email,
                          String telephone,
                          Collection<String> roles,
                          PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEmail(email);
        user.setTelephone(telephone);
        user.setRoles(normalizeRoles(roles, "ROLE_CLIENT"));
        user.setDateCreation(new Date());
        return user;
    }

    private Set<String> normalizeRoles(Collection<String> roles, String fallbackRole) {
        Set<String> normalized = new LinkedHashSet<>();
        if (roles != null) {
            roles.stream()
                    .map(DatabaseInitializer::normalizeRole)
                    .filter(role -> !role.isBlank())
                    .forEach(normalized::add);
        }
        if (normalized.isEmpty()) {
            normalized.add(normalizeRole(fallbackRole));
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

    private boolean isBcryptHash(String value) {
        return value.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
    }
}
