package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCommentRequest {

    @NotBlank(message = "Le commentaire ne peut pas être vide")
    @Schema(description = "Contenu du commentaire", example = "Très intéressant !")
    private String content;
}
