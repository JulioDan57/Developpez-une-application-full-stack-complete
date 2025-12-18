package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    @Schema(description = "ID unique du commentaire", example = "1")
    private Integer commentId;

    @Schema(description = "Contenu du commentaire")
    private String content;

    @Schema(description = "Date de création du commentaire")
    private Instant createdAt;

    @Schema(description = "Auteur du commentaire")
    private String author;
}
