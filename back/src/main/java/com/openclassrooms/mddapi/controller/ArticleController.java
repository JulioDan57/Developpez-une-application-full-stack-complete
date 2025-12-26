package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.service.ArticleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les articles et les commentaires.
 * <p>
 * Ce contrôleur permet de récupérer le feed des articles, de créer un article,
 * d'obtenir un article par ID, et de gérer les commentaires associés aux articles.
 * Toutes les méthodes nécessitent une authentification, sauf indication contraire.
 * </p>
 */
@Validated
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    /**
     * Récupère le feed des articles pour l'utilisateur connecté.
     * Les articles sont triés selon le paramètre {@code order}.
     *
     * @param order l'ordre de tri : "asc" pour croissant, "desc" pour décroissant (par défaut "desc")
     * @return ResponseEntity contenant un {@link ArticleListResponse} avec la liste des articles
     */
    @GetMapping
    public ResponseEntity<ArticleListResponse> getFeed(
            @RequestParam(defaultValue = "desc") String order
    ) {
        return ResponseEntity.ok(articleService.getFeed(order));
    }

    /**
     * Crée un nouvel article pour l'utilisateur connecté.
     *
     * @param request DTO {@link CreateArticleRequest} contenant le titre, le contenu et le subjectId
     * @return ResponseEntity contenant l'article créé sous forme de {@link ArticleDTO}
     */
    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(
            @Valid @RequestBody CreateArticleRequest request
    ) {
        return ResponseEntity.ok(articleService.createArticle(request));
    }

    /**
     * Récupère un article par son identifiant.
     *
     * @param id l'identifiant de l'article (doit être supérieur ou égal à 1)
     * @return ResponseEntity contenant l'article sous forme de {@link ArticleDTO}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable @Min(1) Integer id) {
        return ResponseEntity.ok(articleService.getArticle(id));
    }

    /**
     * Ajoute un commentaire à un article existant.
     *
     * @param id      l'identifiant de l'article (doit être supérieur ou égal à 1)
     * @param request DTO {@link CreateCommentRequest} contenant le contenu du commentaire
     * @return ResponseEntity contenant le commentaire ajouté sous forme de {@link CommentDTO}
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable @Min(1) Integer id,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        return ResponseEntity.ok(articleService.addComment(id, request));
    }

    /**
     * Récupère la liste des commentaires associés à un article.
     *
     * @param id l'identifiant de l'article (doit être supérieur ou égal à 1)
     * @return ResponseEntity contenant la liste des commentaires sous forme de {@link CommentDTO}
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable @Min(1) Integer id) {
        return ResponseEntity.ok(articleService.getComments(id));
    }
}
