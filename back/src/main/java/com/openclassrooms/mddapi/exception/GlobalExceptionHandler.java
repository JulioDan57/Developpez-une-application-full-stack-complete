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

/**
 * Gestionnaire global des exceptions de l'API REST.
 *
 * Cette classe centralise le traitement des exceptions levées dans
 * l'application afin de retourner des réponses HTTP cohérentes,
 * structurées et adaptées aux clients.
 *
 * Chaque exception est traduite en un objet {@link ApiError}
 * contenant le code HTTP, un message explicite et un horodatage.
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================================
    // 1️⃣ DTO Validation errors (@Valid)
    // ================================
    /**
     * Gère les erreurs de validation des DTO annotés avec {@code @Valid}.
     *
     * @param ex exception levée lors de la validation des champs d'un objet
     * @return une réponse HTTP 400 (Bad Request) contenant les erreurs de validation
     */
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
    /**
     * Gère les violations de contraintes sur les paramètres
     * ({@code @PathVariable}, {@code @RequestParam}, etc.).
     *
     * @param ex exception levée lors de la validation des contraintes
     * @return une réponse HTTP 400 (Bad Request) avec le détail des violations
     */
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
    /**
     * Gère les erreurs d'authentification liées à des identifiants incorrects.
     *
     * @param ex exception levée lorsque les informations d'identification sont invalides
     * @return une réponse HTTP 401 (Unauthorized)
     */
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
    /**
     * Gère les erreurs d'accès lorsque l'utilisateur ne dispose pas
     * des autorisations nécessaires.
     *
     * @param ex exception levée lors d'un accès interdit
     * @return une réponse HTTP 403 (Forbidden)
     */
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
    /**
     * Gère les erreurs liées à une ressource inexistante.
     *
     * @param ex exception levée lorsque la ressource demandée n'est pas trouvée
     * @return une réponse HTTP 404 (Not Found)
     */
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
    /**
     * Gère les conflits de données ou de règles métier.
     *
     * @param ex exception levée lorsqu'un conflit est détecté
     * @return une réponse HTTP 409 (Conflict)
     */
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

    /**
     * Gère toutes les autres exceptions non traitées explicitement.
     *
     * @param ex exception générique
     * @return une réponse HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex) {
        ApiError api = new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage(), null, Instant.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(api);
    }

}
