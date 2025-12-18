package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank(message = "Le champ usernameOrEmail est requis")
    @Schema(description = "Email ou nom d'utilisateur", example = "john@example.com")
    private String usernameOrEmail;

    @NotBlank(message = "Le mot de passe est requis")
    @Schema(description = "Mot de passe", example = "Password123!")
    private String password;
}
