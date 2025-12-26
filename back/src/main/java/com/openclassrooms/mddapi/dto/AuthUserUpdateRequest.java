package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilisé pour mettre à jour les informations d'un utilisateur.
 *
 * Les champs sont optionnels : seul le champ fourni sera mis à jour.
 * Ce DTO permet de modifier l'e-mail, le nom d'utilisateur et/ou le mot de passe.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserUpdateRequest {

    /** Nouvelle adresse e-mail de l'utilisateur */
    @Email(message = "Email invalide")
    @Schema(example = "new@mail.com")
    private String email;

    /** Nouveau nom d'utilisateur */
    @Size(min = 3, max = 30, message = "Le nom d'utilisateur doit contenir entre 3 et 30 caractères")
    @Schema(example = "NewUsername")
    private String username;

    /** Nouveau mot de passe de l'utilisateur */
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Schema(example = "NewPassword123!")
    private String password;

}
