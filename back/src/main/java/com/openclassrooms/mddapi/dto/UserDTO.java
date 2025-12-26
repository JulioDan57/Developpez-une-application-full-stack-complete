package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object représentant un utilisateur.
 *
 * Ce DTO contient les informations principales d'un utilisateur,
 * telles que son identifiant, son email et son nom d'utilisateur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /** ID unique de l'utilisateur */
    @Schema(description = "ID unique de l'utilisateur", example = "1")
    private Integer userId;

    /** Adresse e-mail de l'utilisateur */
    @Schema(description = "Adresse e-mail de l'utilisateur", example = "user@example.com")
    private String email;

    /** Nom d'utilisateur choisi par l'utilisateur */
    @Schema(description = "Nom d'utilisateur", example = "JohnDoe")
    private String username;

}
