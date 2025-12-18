package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Schema(description = "ID unique de l'utilisateur", example = "1")
    private Integer userId;

    @Schema(description = "Adresse e-mail de l'utilisateur", example = "user@example.com")
    private String email;

    @Schema(description = "Nom d'utilisateur", example = "JohnDoe")
    private String username;

}
