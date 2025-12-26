package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO représentant le profil complet de l'utilisateur connecté.
 *
 * Contient les informations de base de l'utilisateur ainsi que la liste de ses abonnements.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserProfileResponse {

    /** ID unique de l'utilisateur */
    @Schema(description = "ID unique de l'utilisateur", example = "1")
    private Integer userId;

    /** Adresse e-mail de l'utilisateur */
    @Schema(description = "Adresse e-mail de l'utilisateur", example = "user@example.com")
    private String email;

    /** Nom d'utilisateur */
    @Schema(description = "Nom d'utilisateur", example = "JohnDoe")
    private String username;

    /** Liste des abonnements de l'utilisateur */
    @Schema(description = "Liste des abonnements de l'utilisateur")
    private List<SubscriptionDTO> subscriptions;
}
