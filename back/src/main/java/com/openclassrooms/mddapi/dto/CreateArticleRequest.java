package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
/**
 * Data Transfer Object utilisé pour créer un nouvel article.
 *
 * Ce DTO contient les informations nécessaires pour créer un article,
 * incluant le titre, le contenu et le thème associé.
 */
@Data
public class CreateArticleRequest {

    /** Titre de l'article */
    @NotBlank(message = "Le titre est obligatoire")
    @Schema(description = "Titre de l'article", example = "Mon article")
    private String title;

    /** Contenu textuel de l'article */
    @NotBlank(message = "Le contenu est obligatoire")
    @Schema(description = "Contenu de l'article", example = "Contenu détaillé...")
    private String content;

    /** ID du thème (subject) auquel l'article est associé */
    @NotNull(message = "Le sujet est obligatoire")
    @Schema(description = "ID du thème associé", example = "1")
    private Integer subjectId;
}