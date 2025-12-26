package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implémentation personnalisée de {@link UserDetails} utilisée par Spring Security.
 *
 * Cette classe encapsule les informations d'un utilisateur de l'application MDD
 * nécessaires pour l'authentification et la gestion des rôles.
 *
 *
 * Elle expose notamment :
 * <ul>
 *     <li>l'identifiant de l'utilisateur (userId),</li>
 *     <li>l'email et le username,</li>
 *     <li>le mot de passe encodé,</li>
 *     <li>le login utilisé pour l'authentification (email ou username).</li>
 * </ul>
 *
 */
@Getter
public class CustomUserDetails implements UserDetails {

    /** Identifiant unique de l'utilisateur. */
    private final Integer userId;

    /** Email de l'utilisateur. */
    private final String email;

    /** Nom d'utilisateur (username) de l'utilisateur. */
    private final String username;   // <-- Ajout important

    /** Mot de passe encodé de l'utilisateur. */
    private final String password;

    /** Login utilisé pour l'authentification (email ou username). */
    private final String loginUsed; // email OU username

    /**
     * Constructeur principal qui initialise les informations de l'utilisateur à partir
     * de l'entité {@link User} et du login utilisé.
     *
     * @param user l'entité {@link User} correspondant à l'utilisateur
     * @param loginUsed le login utilisé pour la connexion (email ou username)
     */
    public CustomUserDetails(User user,  String loginUsed) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.username = user.getUsername(); // <-- ici aussi
        this.password = user.getPassword();
        this.loginUsed = loginUsed;
    }

    /**
     * Retourne les autorités (rôles) de l'utilisateur.
     * Ici, tous les utilisateurs ont le rôle "ROLE_USER".
     *
     * @return une collection d'objets {@link GrantedAuthority}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * Retourne le nom d'utilisateur utilisé pour l'authentification.
     * Dans cette implémentation, il s'agit du login fourni lors de la connexion,
     * soit l'email soit le username.
     *
     * @return le login utilisé
     */
    @Override
    public String getUsername() {
        return loginUsed;
    }

}

