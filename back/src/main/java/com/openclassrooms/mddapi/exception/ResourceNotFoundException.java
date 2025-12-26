package com.openclassrooms.mddapi.exception;

/**
 * Exception levée lorsqu'une ressource demandée n'est pas trouvée.
 *
 * Cette exception est généralement utilisée dans la couche service ou contrôleur
 * pour signaler qu'une entité (utilisateur, article, commentaire, etc.)
 * n'existe pas en base de données.
 *
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construit une nouvelle exception {@code ResourceNotFoundException}
     * avec un message décrivant la cause de l'erreur.
     *
     * @param message le message détaillant la ressource non trouvée
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
