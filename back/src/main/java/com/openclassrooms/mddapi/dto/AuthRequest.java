package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO représentant une requête d'authentification.
 *
 * Ce DTO est utilisé pour envoyer les informations nécessaires à la connexion d'un utilisateur,
 * soit par son email, soit par son nom d'utilisateur, accompagné de son mot de passe.
 */
@Data
public class AuthRequest {

    /** Email ou nom d'utilisateur de l'utilisateur */
    @NotBlank(message = "Le champ usernameOrEmail est requis")
    @Schema(description = "Email ou nom d'utilisateur", example = "john@example.com")
    private String usernameOrEmail;

    /** Mot de passe de l'utilisateur  */
    @NotBlank(message = "Le mot de passe est requis")
    @Schema(description = "Mot de passe", example = "Password123!")
    private String password;
}
