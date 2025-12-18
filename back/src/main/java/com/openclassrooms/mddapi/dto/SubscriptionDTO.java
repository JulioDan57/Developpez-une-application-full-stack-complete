package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
public class SubscriptionDTO {

    @Schema(description = "ID unique de l'abonnement", example = "1")
    private Integer subscriptionId;
    private Integer subjectId;
    private String subjectName;
    private String subjectDescription;

}
