package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Réponse contenant une liste d'articles")
public class ArticleListResponse {

    @Schema(description = "Liste d'articles")
    private List<ArticleDTO> articles;
}
