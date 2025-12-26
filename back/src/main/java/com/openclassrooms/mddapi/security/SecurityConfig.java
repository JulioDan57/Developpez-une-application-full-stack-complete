package com.openclassrooms.mddapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

/**
 * Configuration de la sécurité Spring Security pour l'application MDD.
 *
 * Cette configuration inclut :
 * <ul>
 *     <li>Désactivation de CSRF</li>
 *     <li>Gestion stateless des sessions (JWT)</li>
 *     <li>Filtrage des requêtes HTTP</li>
 *     <li>Filtrage CORS pour autoriser le front-end Angular</li>
 *     <li>Liste blanche pour Swagger et endpoints publics d'authentification</li>
 * </ul>
 *
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    // Liste blanche Swagger / ressources publiques
    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui/index.html"
    };

    /**
     * Configure la chaîne de filtres de sécurité pour HTTP.
     *
     * Autorise les endpoints Swagger et d'authentification sans JWT.
     * Toutes les autres requêtes nécessitent un JWT valide.
     * Active la politique de session stateless.
     * Désactive CSRF et configure CORS.
     *
     *
     * @param http      objet HttpSecurity fourni par Spring
     * @param jwtFilter filtre JWT personnalisé
     * @return la {@link SecurityFilterChain} configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Déclare le bean PasswordEncoder pour l'encodage des mots de passe.
     *
     * Utilise BCrypt avec un hash sécurisé.
     *
     *
     * @return {@link PasswordEncoder} utilisant BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Déclare le bean AuthenticationManager nécessaire à Spring Security.
     *
     * @return {@link AuthenticationManager} fourni par l'AuthenticationConfiguration
     * @throws Exception en cas d'erreur
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    // 🌐 Configuration CORS pour autoriser Angular
    /**
     * Configure CORS pour autoriser les requêtes provenant du front-end Angular.
     *
     * Autorise : <br>
     * - Origine : http://localhost:4200<br>
     * - Headers : tous<br>
     * - Méthodes : GET, POST, PUT, DELETE, OPTIONS<br>
     *
     *
     * @return {@link UrlBasedCorsConfigurationSource} configuré
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
