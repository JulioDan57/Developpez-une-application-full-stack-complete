package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse après la mise à jour des informations d'un utilisateur.
 *
 * Contient le nouveau jeton JWT permettant à l'utilisateur de continuer à s'authentifier
 * avec ses informations mises à jour.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserUpdateResponse {

    /** Jeton JWT d'authentification mis à jour */
    @Schema(description = "Jeton JWT d'authentification")
    private String token;
}
