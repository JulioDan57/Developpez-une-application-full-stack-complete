package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentDTO addComment(Article article, User user, String content) {
        Comment comment = Comment.builder()
                .content(content)
                .article(article)
                .user(user)
                .build();

        Comment saved = commentRepository.save(comment);

        // Transformation en DTO
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(saved.getCommentId());
        dto.setContent(saved.getContent());
        dto.setCreatedAt(saved.getCreatedAt());
        dto.setAuthor(saved.getUser().getUsername());

        return dto;
    }

    public List<Comment> getCommentsByArticle(Article article) {
        return commentRepository.findByArticleOrderByCreatedAtAsc(article);
    }
}
