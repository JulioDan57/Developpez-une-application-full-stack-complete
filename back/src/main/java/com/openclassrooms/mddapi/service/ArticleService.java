package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.entity.*;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Service métier gérant la logique liée aux articles, au fil d'actualité
 * et aux commentaires.
 *
 * Cette classe centralise les règles métiers concernant :
 * <ul>
 *     <li>La récupération du feed utilisateur</li>
 *     <li>La création et la consultation d'articles</li>
 *     <li>La gestion des commentaires</li>
 * </ul>
 * Elle s'appuie sur Spring Data JPA pour l'accès aux données
 * et sur {@link SecurityUtils} pour identifier l'utilisateur connecté.
 *
 */
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final SubjectRepository subjectRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SecurityUtils securityUtils;
    private final CommentService commentService;

    // ============================================================
    // FEED + ARTICLES
    // ============================================================
    /**
     * Récupère le fil d'actualité (feed) de l'utilisateur connecté.
     *
     * Le feed est composé des articles appartenant aux sujets auxquels
     * l'utilisateur est abonné, triés par date de création.
     *
     *
     * @param order ordre de tri des articles :
     *              <ul>
     *                  <li>{@code "asc"} : du plus ancien au plus récent</li>
     *                  <li>{@code "desc"} : du plus récent au plus ancien</li>
     *              </ul>
     * @return {@link ArticleListResponse} contenant la liste des articles du feed
     */
    public ArticleListResponse getFeed(String order) {
        User currentUser = securityUtils.getCurrentUser();

        // 1️⃣ Récupérer les subscriptions UNE SEULE FOIS
        List<Subscription> subscriptions = subscriptionRepository.findByUser(currentUser);

        if (subscriptions.isEmpty()) {
            return new ArticleListResponse(List.of());
        }

        // 2️⃣ Subjects abonnés
        List<Subject> subscribedSubjects = subscriptions.stream()
                .map(Subscription::getSubject)
                .toList();

        // 3️⃣ IDs abonnés (pour le mapping DTO)
        Set<Integer> subscribedSubjectIds = subscribedSubjects.stream()
                .map(Subject::getSubjectId)
                .collect(Collectors.toSet());

        // 4️⃣ Articles
        List<Article> articles =
                "asc".equalsIgnoreCase(order)
                        ? articleRepository.findBySubjectInOrderByCreatedAtAsc(subscribedSubjects)
                        : articleRepository.findBySubjectInOrderByCreatedAtDesc(subscribedSubjects);

        List<ArticleDTO> articleDTOs = articles.stream()
                .map(article -> mapToDTO(article, subscribedSubjectIds))
                .collect(Collectors.toList());

        ArticleListResponse response = new ArticleListResponse();
        response.setArticles(articleDTOs);

        return response;
    }

    /**
     * Crée un nouvel article pour l'utilisateur connecté.
     *
     * @param request données nécessaires à la création de l'article
     * @return {@link ArticleDTO} représentant l'article créé
     * @throws ResourceNotFoundException si le sujet associé n'existe pas
     */
    public ArticleDTO createArticle(CreateArticleRequest request) {
        User user = securityUtils.getCurrentUser();

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sujet introuvable : " + request.getSubjectId())
                );

        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .subject(subject)
                .build();

        articleRepository.save(article);

        // L'auteur est forcément abonné ou non → recalcul simple
        Set<Integer> subscribedSubjectIds = subscriptionRepository.findByUser(user)
                .stream()
                .map(sub -> sub.getSubject().getSubjectId())
                .collect(Collectors.toSet());

        return mapToDTO(article, subscribedSubjectIds);
    }

    /**
     * Récupère un article par son identifiant.
     *
     * @param id identifiant de l'article
     * @return {@link ArticleDTO} correspondant à l'article demandé
     * @throws ResourceNotFoundException si l'article n'existe pas
     */
    public ArticleDTO getArticle(Integer id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() ->        new ResourceNotFoundException("Article introuvable : " + id)
                );

        User currentUser = securityUtils.getCurrentUser();

        Set<Integer> subscribedSubjectIds = subscriptionRepository.findByUser(currentUser)
                .stream()
                .map(sub -> sub.getSubject().getSubjectId())
                .collect(Collectors.toSet());

        return mapToDTO(article, subscribedSubjectIds);
    }

    // ============================================================
    // COMMENTS
    // ============================================================
    /**
     * Ajoute un commentaire à un article.
     *
     * @param articleId identifiant de l'article commenté
     * @param request   contenu du commentaire
     * @return {@link CommentDTO} représentant le commentaire créé
     * @throws ResourceNotFoundException si l'article n'existe pas
     */
    public CommentDTO addComment(Integer articleId, CreateCommentRequest request) {
        User user = securityUtils.getCurrentUser();

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Article introuvable : " + articleId)
                );

        return commentService.addComment(article, user, request.getContent());
    }

    public List<CommentDTO> getComments(Integer articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Article introuvable : " + articleId)
                );

        return commentService.getCommentsByArticle(article)
                .stream()
                .map(this::mapToCommentDTO)
                .collect(Collectors.toList());
    }

    // ============================================================
    // MAPPING
    // ============================================================

    private ArticleDTO mapToDTO(Article article, Set<Integer> subscribedSubjectIds) {

        boolean subscribed = subscribedSubjectIds.contains(
                article.getSubject().getSubjectId()
        );

        ArticleDTO dto = new ArticleDTO();
        dto.setArticleId(article.getArticleId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setCreatedAt(article.getCreatedAt());

        dto.setAuthor(new UserDTO(
                article.getUser().getUserId(),
                article.getUser().getEmail(),
                article.getUser().getUsername()
        ));

        dto.setSubject(new SubjectDTO(
                article.getSubject().getSubjectId(),
                article.getSubject().getName(),
                article.getSubject().getDescription(),
                subscribed
        ));

        dto.setComments(
                commentService.getCommentsByArticle(article)
                        .stream()
                        .map(this::mapToCommentDTO)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    private CommentDTO mapToCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setAuthor(comment.getUser().getUsername());
        return dto;
    }
}
