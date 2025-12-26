package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service gérant la logique métier liée aux commentaires.
 *
 * Ce service fournit les fonctionnalités suivantes :
 * <ul>
 *     <li>Ajouter un commentaire à un article</li>
 *     <li>Récupérer la liste des commentaires d'un article triés par date</li>
 * </ul>
 *
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * Ajoute un commentaire à un article par un utilisateur donné.
     *
     * @param article article auquel le commentaire est associé
     * @param user utilisateur auteur du commentaire
     * @param content contenu du commentaire
     * @return {@link CommentDTO} représentant le commentaire ajouté
     */
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

    /**
     * Récupère la liste des commentaires d'un article, triés par date de création croissante.
     *
     * @param article article pour lequel récupérer les commentaires
     * @return liste des {@link Comment} associés à l'article
     */
    public List<Comment> getCommentsByArticle(Article article) {
        return commentRepository.findByArticleOrderByCreatedAtAsc(article);
    }
}
