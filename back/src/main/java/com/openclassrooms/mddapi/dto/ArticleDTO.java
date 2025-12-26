package com.openclassrooms.mddapi.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object représentant un article.
 *
 * Ce DTO contient les informations principales d'un article,
 * y compris son auteur, son thème et la liste des commentaires associés.
 */
@Data
public class ArticleDTO {

    /** ID unique de l'article */
    @Schema(description = "ID unique de l'article", example = "1")
    private Integer articleId;

    /** Titre de l'article */
    @Schema(description = "Titre de l'article", example = "Introduction à Java")
    private String title;

    /** Contenu textuel de l'article */
    @Schema(description = "Contenu de l'article")
    private String content;

    /** Date et heure de création de l'article */
    @Schema(description = "Date de création de l'article")
    private Instant createdAt;

    /** Informations sur l'auteur de l'article */
    @Schema(description = "Auteur de l'article")
    private UserDTO author;

    /** Thème associé à l'article */
    @Schema(description = "Thème associé à l'article")
    private SubjectDTO subject;

    /** Liste des commentaires liés à l'article */
    @Schema(description = "Commentaires associés à l'article")
    private List<CommentDTO> comments;
}
