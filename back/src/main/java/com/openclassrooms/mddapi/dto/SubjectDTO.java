package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {

    @Schema(description = "ID unique du thème", example = "1")
    private Integer subjectId;

    @Schema(description = "Nom du thème", example = "Technologie")
    private String name;

    @Schema(description = "Description détaillée du thème")
    private String description;

    @Schema(description = "Indique si l'utilisateur est abonné à ce thème")
    private boolean subscribed;
}
