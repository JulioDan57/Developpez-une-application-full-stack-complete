package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Data Transfer Object représentant un commentaire.
 *
 * Ce DTO contient les informations essentielles d'un commentaire,
 * y compris son contenu, la date de création et l'auteur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    /** ID unique du commentaire */
    @Schema(description = "ID unique du commentaire", example = "1")
    private Integer commentId;

    /** Contenu textuel du commentaire */
    @Schema(description = "Contenu du commentaire")
    private String content;

    /** Date et heure de création du commentaire */
    @Schema(description = "Date de création du commentaire")
    private Instant createdAt;

    /** Nom de l'auteur du commentaire */
    @Schema(description = "Auteur du commentaire")
    private String author;
}
