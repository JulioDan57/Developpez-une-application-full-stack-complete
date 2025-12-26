package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilitaires pour accéder aux informations de sécurité et à l'utilisateur connecté.
 *
 * Fournit une méthode pour récupérer l'utilisateur actuellement authentifié
 * à partir du contexte de sécurité Spring Security.
 *
 */
@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    /**
     * Récupère l'utilisateur actuellement connecté.
     *
     * Cette méthode utilise le SecurityContextHolder de Spring Security
     * pour obtenir les détails de l'utilisateur authentifié.
     *
     *
     * @return l'entité {@link User} correspondant à l'utilisateur connecté
     * @throws RuntimeException si l'utilisateur n'est pas trouvé en base de données
     */
    public User getCurrentUser() {
        CustomUserDetails cud = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findById(cud.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }
}
