package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateUserRequest {

    @Schema(example = "new@mail.com")
    private String email;

    @Schema(example = "NewUsername")
    private String username;

    @Schema(example = "NewPassword123!")
    private String password;

}
