package org.example.lbateau.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig {

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:4200",
            "http://127.0.0.1:4200",
            "http://localhost:5173",
            "http://127.0.0.1:5173",
            "http://localhost:3000",
            "https://l-bateau.vercel.app"
    );

    private static final List<String> ALLOWED_METHODS = List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
    );

    private static final List<String> ALLOWED_HEADERS = List.of(
            "Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"
    );

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(ALLOWED_ORIGINS);
        config.setAllowedMethods(ALLOWED_METHODS);
        config.setAllowedHeaders(ALLOWED_HEADERS);
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(ALLOWED_ORIGINS.toArray(String[]::new))
                        .allowedMethods(ALLOWED_METHODS.toArray(String[]::new))
                        .allowedHeaders(ALLOWED_HEADERS.toArray(String[]::new))
                        .exposedHeaders("Authorization")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
