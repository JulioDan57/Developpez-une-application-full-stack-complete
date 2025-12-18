package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserProfileResponse {

    @Schema(description = "ID unique de l'utilisateur", example = "1")
    private Integer userId;

    @Schema(description = "Adresse e-mail de l'utilisateur", example = "user@example.com")
    private String email;

    @Schema(description = "Nom d'utilisateur", example = "JohnDoe")
    private String username;

    private List<SubscriptionDTO> subscriptions;
}
