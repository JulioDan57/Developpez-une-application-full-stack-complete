package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object représentant la réponse après l'enregistrement d'un utilisateur.
 *
 * Ce DTO contient le jeton JWT généré permettant à l'utilisateur de s'authentifier.
 */
@Data
@AllArgsConstructor
public class RegisterResponse {

    /** Jeton JWT généré pour l'utilisateur nouvellement enregistré */
    @Schema(description = "Jeton JWT généré")
    private String token;
}
