package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service personnalisé implémentant {@link UserDetailsService} pour Spring Security.
 *
 * Il permet de charger un utilisateur à partir de son email ou de son nom d'utilisateur
 * lors du processus d'authentification.
 *
 *
 * Cette classe est marquée {@link Primary} pour être utilisée comme implémentation par défaut
 * de {@link UserDetailsService} dans le contexte Spring Security.
 *
 */
@Service
@Primary
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /** Repository permettant l'accès aux données utilisateurs. */
    private final UserRepository userRepository;

    /**
     * Charge un utilisateur par son email ou son nom d'utilisateur.
     *
     * @param emailOrUsername l'email ou le nom d'utilisateur fourni pour la connexion
     * @return un objet {@link UserDetails} contenant les informations nécessaires à Spring Security
     * @throws UsernameNotFoundException si aucun utilisateur ne correspond à l'email ou username fourni
     */
    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        return new CustomUserDetails(user, emailOrUsername);
    }
}
