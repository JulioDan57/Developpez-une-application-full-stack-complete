package com.openclassrooms.mddapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

/**
 * Objet représentant une réponse d'erreur standardisée de l'API.
 *
 * Cette classe est utilisée pour encapsuler les informations d'erreur
 * retournées par l'API en cas d'exception, notamment lors des erreurs
 * de validation, de ressources non trouvées ou de conflits.
 *
 */
@Data
@AllArgsConstructor
public class ApiError {
    /**
     * Code HTTP associé à l'erreur (ex: 400, 404, 409, 500).
     */
    private int status;

    /**
     * Message global décrivant l'erreur.
     */
    private String message;

    /**
     * Détails des erreurs par champ (principalement utilisé pour les erreurs de validation).
     * La clé correspond au nom du champ et la valeur au message d'erreur associé.
     */
    private Map<String, String> errors;

    /**
     * Date et heure de génération de l'erreur.
     */
    private Instant timestamp;
}
