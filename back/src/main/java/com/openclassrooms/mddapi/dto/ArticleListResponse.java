package com.openclassrooms.mddapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ArticleListResponse {

    @Schema(description = "Liste d'articles")
    private List<ArticleDTO> articles;
}
