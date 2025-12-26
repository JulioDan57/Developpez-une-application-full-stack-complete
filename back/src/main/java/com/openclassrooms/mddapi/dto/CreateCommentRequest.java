package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object utilisé pour créer un nouveau commentaire.
 *
 * Ce DTO contient les informations nécessaires pour ajouter un commentaire
 * à un article, principalement le contenu textuel du commentaire.
 */
@Data
public class CreateCommentRequest {

    /** Contenu textuel du commentaire */
    @NotBlank(message = "Le commentaire ne peut pas être vide")
    @Schema(description = "Contenu du commentaire", example = "Très intéressant !")
    private String content;
}
