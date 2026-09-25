package org.example.lbateau.Security;

import org.example.lbateau.Entity.User;
import org.example.lbateau.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(CustomUserDetailsService::normalizeRole)
                .filter(role -> !role.isBlank())
                .distinct()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword() != null ? user.getPassword() : "",
                authorities
        );
    }

    private static String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "";
        }
        String value = role.trim().toUpperCase();
        return value.startsWith("ROLE_") ? value : "ROLE_" + value;
    }
}
