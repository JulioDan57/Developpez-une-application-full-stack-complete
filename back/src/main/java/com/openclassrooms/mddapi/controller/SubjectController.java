package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des sujets (thèmes) et des abonnements.
 * <p>
 * Permet de récupérer tous les sujets, de filtrer par abonnements et de gérer
 * l'abonnement / désabonnement des utilisateurs aux sujets.
 * </p>
 */
@Validated
@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    /**
     * Récupère tous les sujets.
     *
     * @param subscribed si true → retourne uniquement les sujets abonnés,
     *                   si false → retourne uniquement les sujets non abonnés,
     *                   si null → retourne tous les sujets
     * @return ResponseEntity contenant la liste des {@link SubjectDTO}
     */
    @Operation(summary = "Récupérer tous les sujets")
    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects(
            @RequestParam(required = false) Boolean subscribed) {

        if (subscribed == null) {
            // Aucun filtre → retourner tous les sujets
            return ResponseEntity.ok(subjectService.getAllSubjectsDTO());
        } else if (subscribed) {
            // Filtrer uniquement les sujets abonnés
            return ResponseEntity.ok(subjectService.getSubscribedSubjectsDTO());
        } else {
            // Filtrer uniquement les sujets non abonnés
            return ResponseEntity.ok(subjectService.getUnsubscribedSubjectsDTO());
        }
    }

    /**
     * Abonne l'utilisateur connecté au sujet identifié par l'ID.
     *
     * @param id l'identifiant du sujet auquel s'abonner (doit être >= 1)
     * @return ResponseEntity vide avec code HTTP 200 si succès
     */
    @Operation(summary = "S'abonner à un sujet")
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Void> subscribe(@PathVariable @Min(1) Integer id) {
        subjectService.subscribe(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Désabonne l'utilisateur connecté du sujet identifié par l'ID.
     *
     * @param id l'identifiant du sujet dont se désabonner (doit être >= 1)
     * @return ResponseEntity vide avec code HTTP 200 si succès
     */
    @Operation(summary = "Se désabonner d'un sujet")
    @PostMapping("/{id}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@PathVariable @Min(1) Integer id) {
        subjectService.unsubscribe(id);
        return ResponseEntity.ok().build();
    }
}
