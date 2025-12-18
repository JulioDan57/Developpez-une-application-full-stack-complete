package com.openclassrooms.mddapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================================
    // 1️⃣ DTO Validation errors (@Valid)
    // ================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        ApiError api = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request",
                errors,
                Instant.now()
        );

        return ResponseEntity.badRequest().body(api);
    }

    // ============================================
    // 2️⃣ @Valid on PathVariable / RequestParam
    // ============================================
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolations(ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

        ApiError api = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request",
                errors,
                Instant.now()
        );

        return ResponseEntity.badRequest().body(api);
    }

    // ================================
    // 3️⃣ Wrong credentials
    // ================================
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {

        ApiError api = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                "Email ou mot de passe incorrect",
                null,
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(api);
    }

    // ================================
    // 4️⃣ Forbidden
    // ================================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {

        ApiError api = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                "Accès refusé",
                null,
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(api);
    }

    // ================================
    // 5️⃣ Not Found
    // ================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {

        ApiError api = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null,
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(api);
    }

    // ================================
    // 6️⃣ Fallback
    // ================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {

        ApiError api = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Une erreur interne est survenue",
                null,
                Instant.now()
        );

        return ResponseEntity.internalServerError().body(api);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex) {
        ApiError api = new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage(), null, Instant.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(api);
    }

}
