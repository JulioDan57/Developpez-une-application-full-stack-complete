package com.openclassrooms.mddapi.exception;

/**
 * Exception levée lorsqu'une opération entraîne un conflit métier.
 *
 * Cette exception est généralement utilisée pour signaler une violation
 * de règles fonctionnelles, par exemple :
 *
 * <ul>
 *     <li>Tentative de création d'une ressource déjà existante</li>
 *     <li>Abonnement déjà existant</li>
 *     <li>Action interdite car l'état courant ne le permet pas</li>
 * </ul>
 *
 * Elle est typiquement associée au code HTTP {@code 409 CONFLICT}.
 */
public class ConflictException extends RuntimeException {

    /**
     * Construit une nouvelle exception de conflit avec un message explicite.
     *
     * @param message message décrivant la cause du conflit
     */
    public ConflictException(String message) {
        super(message);
    }
}
