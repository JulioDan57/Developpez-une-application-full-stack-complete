package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object utilisé pour l'enregistrement d'un nouvel utilisateur.
 *
 * Ce DTO contient les informations nécessaires à la création d'un compte,
 * à savoir l'email, le nom d'utilisateur et le mot de passe.
 */
@Data
public class RegisterRequest {

    /** Adresse e-mail de l'utilisateur */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    @Schema(description = "Email", example = "user@example.com")
    private String email;

    /** Nom d'utilisateur choisi par l'utilisateur */
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 30, message = "Le nom d'utilisateur doit contenir entre 3 et 30 caractères")
    @Schema(description = "Nom d'utilisateur", example = "JohnDoe")
    private String username;

    /** Mot de passe de l'utilisateur */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Schema(description = "Mot de passe", example = "Password123!")
    private String password;
}
