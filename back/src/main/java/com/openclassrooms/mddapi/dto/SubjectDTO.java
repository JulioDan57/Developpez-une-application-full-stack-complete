package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;


/**
 * Data Transfer Object représentant un thème (subject).
 *
 * Ce DTO contient les informations principales d'un thème ainsi que le statut
 * d'abonnement de l'utilisateur connecté.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {

    /** ID unique du thème */
    @Schema(description = "ID unique du thème", example = "1")
    private Integer subjectId;

    /** Nom du thème */
    @Schema(description = "Nom du thème", example = "Technologie")
    private String name;

    /** Description détaillée du thème */
    @Schema(description = "Description détaillée du thème")
    private String description;

    /** Indique si l'utilisateur est abonné à ce thème */
    @Schema(description = "Indique si l'utilisateur est abonné à ce thème")
    private boolean subscribed;
}
