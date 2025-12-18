package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateArticleRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Schema(description = "Titre de l'article", example = "Mon article")
    private String title;

    @NotBlank(message = "Le contenu est obligatoire")
    @Schema(description = "Contenu de l'article", example = "Contenu détaillé...")
    private String content;

    @NotNull(message = "Le sujet est obligatoire")
    @Schema(description = "ID du thème associé", example = "1")
    private Integer subjectId;
}