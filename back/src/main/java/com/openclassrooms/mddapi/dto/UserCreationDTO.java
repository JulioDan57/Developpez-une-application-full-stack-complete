package com.openclassrooms.mddapi.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserCreationDTO {

    @Schema(description = "Adresse e-mail pour inscription", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "Nom d'utilisateur", example = "JohnDoe", required = true)
    private String username;

    @Schema(description = "Mot de passe sécurisé", example = "Passw0rd!", required = true)
    private String password;
}
