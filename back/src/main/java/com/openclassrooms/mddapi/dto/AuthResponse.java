package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO représentant la réponse d'authentification.
 *
 * Ce DTO contient le jeton JWT généré lors de la connexion de l'utilisateur.
 */
@Data
@AllArgsConstructor
public class AuthResponse {

    /** Jeton JWT utilisé pour authentifier les requêtes de l'utilisateur */
    @Schema(description = "Jeton JWT d'authentification")
    private String token;
}
