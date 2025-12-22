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

@Validated
@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

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

    @Operation(summary = "S'abonner à un sujet")
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Void> subscribe(@PathVariable @Min(1) Integer id) {
        subjectService.subscribe(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Se désabonner d'un sujet")
    @PostMapping("/{id}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@PathVariable @Min(1) Integer id) {
        subjectService.unsubscribe(id);
        return ResponseEntity.ok().build();
    }
}
