package com.openclassrooms.mddapi.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

@Data
public class ArticleDTO {

    @Schema(description = "ID unique de l'article", example = "1")
    private Integer articleId;

    @Schema(description = "Titre de l'article", example = "Introduction à Java")
    private String title;

    @Schema(description = "Contenu de l'article")
    private String content;

    @Schema(description = "Date de création de l'article")
    private Instant createdAt;

    @Schema(description = "Auteur de l'article")
    private UserDTO author;

    @Schema(description = "Thème associé à l'article")
    private SubjectDTO subject;

    @Schema(description = "Commentaires associés à l'article")
    private List<CommentDTO> comments;
}
