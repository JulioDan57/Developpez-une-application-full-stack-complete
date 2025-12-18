package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserUpdateRequest {

    @Email(message = "Email invalide")
    @Schema(example = "new@mail.com")
    private String email;

    @Size(min = 3, max = 30, message = "Le nom d'utilisateur doit contenir entre 3 et 30 caractères")
    @Schema(example = "NewUsername")
    private String username;

    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Schema(example = "NewPassword123!")
    private String password;

}
