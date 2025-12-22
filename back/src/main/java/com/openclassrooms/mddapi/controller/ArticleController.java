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

@Validated
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<ArticleListResponse> getFeed(
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
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable @Min(1) Integer id) {
        return ResponseEntity.ok(articleService.getArticle(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable @Min(1) Integer id,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        return ResponseEntity.ok(articleService.addComment(id, request));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable @Min(1) Integer id) {
        return ResponseEntity.ok(articleService.getComments(id));
    }
}
