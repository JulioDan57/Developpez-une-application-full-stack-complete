package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getFeed(
            @RequestParam(defaultValue = "desc") String order
    ) {
        return ResponseEntity.ok(articleService.getFeed(order));
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(
            @Valid @RequestBody CreateArticleRequest request
    ) {
        return ResponseEntity.ok(articleService.createArticle(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable Integer id) {
        return ResponseEntity.ok(articleService.getArticle(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Integer id,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        return ResponseEntity.ok(articleService.addComment(id, request));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Integer id) {
        return ResponseEntity.ok(articleService.getComments(id));
    }
}
