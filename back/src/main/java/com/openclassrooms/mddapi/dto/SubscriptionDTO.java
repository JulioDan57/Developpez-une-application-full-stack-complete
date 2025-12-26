package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object représentant un abonnement d'utilisateur à un thème (subject).
 *
 * Ce DTO contient les informations de l'abonnement ainsi que les détails du thème associé.
 */
@Data
@AllArgsConstructor
public class SubscriptionDTO {

    /** ID unique de l'abonnement */
    @Schema(description = "ID unique de l'abonnement", example = "1")
    private Integer subscriptionId;

    /** ID du thème associé à l'abonnement */
    @Schema(description = "ID du thème associé à l'abonnement", example = "1")
    private Integer subjectId;

    /** Nom du thème associé à l'abonnement */
    @Schema(description = "Nom du thème associé à l'abonnement", example = "Technologie")
    private String subjectName;

    /** Description du thème associé à l'abonnement */
    @Schema(description = "Description du thème associé à l'abonnement")
    private String subjectDescription;

}
