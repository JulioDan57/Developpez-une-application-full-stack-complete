package com.openclassrooms.mddapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
public class ApiError {
    private int status;
    private String message;
    private Map<String, String> errors;
    private Instant timestamp;
}
