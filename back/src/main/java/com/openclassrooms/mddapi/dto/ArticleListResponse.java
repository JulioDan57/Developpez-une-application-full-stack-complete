package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Réponse contenant une liste d'articles.
 *
 * Ce DTO est utilisé pour renvoyer plusieurs articles dans une seule réponse
 * lors de la récupération du feed ou d'une recherche d'articles.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Réponse contenant une liste d'articles")
public class ArticleListResponse {

    /** Liste des articles  */
    @Schema(description = "Liste d'articles")
    private List<ArticleDTO> articles;
}
