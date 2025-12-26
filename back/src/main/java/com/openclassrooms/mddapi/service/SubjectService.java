package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.Subscription;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.ConflictException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service gérant la logique métier des sujets (Subjects) et des abonnements.
 *
 * Fournit des fonctionnalités pour :
 * <ul>
 *     <li>Récupérer la liste des sujets (avec ou sans filtre sur l'abonnement)</li>
 *     <li>S'abonner et se désabonner à un sujet</li>
 *     <li>Mapper les entités Subject vers SubjectDTO avec l'information d'abonnement</li>
 * </ul>
 *
 */
@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SecurityUtils securityUtils;

    // ============================================================
    // GET ALL SUBJECTS (with subscribed flag)
    // ============================================================
    /**
     * Récupère tous les sujets sous forme de DTO.
     * Le champ "subscribed" indique si l'utilisateur courant est abonné ou non.
     *
     * @return liste des {@link SubjectDTO} représentant tous les sujets
     */
    public List<SubjectDTO> getAllSubjectsDTO() {
        User user = securityUtils.getCurrentUser();

        return subjectRepository.findAll().stream()
                .map(subject -> mapToDTO(subject, user))
                .collect(Collectors.toList());
    }

    // ============================================================
    // GET ONLY SUBSCRIBED SUBJECTS
    // ============================================================
    /**
     * Récupère uniquement les sujets auxquels l'utilisateur courant est abonné.
     *
     * @return liste des {@link SubjectDTO} représentant les sujets abonnés
     */
    public List<SubjectDTO> getSubscribedSubjectsDTO() {
        User user = securityUtils.getCurrentUser();

        return subjectRepository.findAll().stream()
                .filter(subject -> subscriptionRepository.existsByUserAndSubject(user, subject))
                .map(subject -> mapToDTO(subject, user))
                .collect(Collectors.toList());
    }

    // ============================================================
    // GET ONLY UNSUBSCRIBED SUBJECTS
    // ============================================================
    /**
     * Récupère uniquement les sujets auxquels l'utilisateur courant n'est pas abonné.
     *
     * @return liste des {@link SubjectDTO} représentant les sujets non abonnés
     */
    public List<SubjectDTO> getUnsubscribedSubjectsDTO() {
        User user = securityUtils.getCurrentUser();

        return subjectRepository.findAll().stream()
                .filter(subject -> !subscriptionRepository.existsByUserAndSubject(user, subject))
                .map(subject -> mapToDTO(subject, user))
                .collect(Collectors.toList());
    }


    // ============================================================
    // SUBSCRIBE
    // ============================================================
    /**
     * Abonne l'utilisateur courant à un sujet donné.
     * Si l'utilisateur est déjà abonné, une {@link ConflictException} est levée.
     *
     * @param subjectId identifiant du sujet
     * @throws ConflictException si l'utilisateur est déjà abonné
     * @throws ResourceNotFoundException si le sujet n'existe pas
     */
    public void subscribe(Integer subjectId) {
        User user = securityUtils.getCurrentUser();
        Subject subject = getSubjectOrThrow(subjectId);

        // Vérifier si déjà abonné → exception claire
        if (subscriptionRepository.existsByUserAndSubject(user, subject)) {
            throw new ConflictException("Vous êtes déjà abonné à ce sujet.");

        }

        // Créer l'abonnement
        Subscription subscription = new Subscription(user, subject);
        subscriptionRepository.save(subscription);
    }

    // ============================================================
    // UNSUBSCRIBE
    // ============================================================
    /**
     * Désabonne l'utilisateur courant d'un sujet donné.
     * Si l'abonnement existe, il est supprimé.
     *
     * @param subjectId identifiant du sujet
     * @throws ResourceNotFoundException si le sujet n'existe pas
     */
    public void unsubscribe(Integer subjectId) {
        User user = securityUtils.getCurrentUser();
        Subject subject = getSubjectOrThrow(subjectId);

        subscriptionRepository.findByUserAndSubject(user, subject)
                .ifPresent(subscriptionRepository::delete);
    }

    // ============================================================
    // MAPPING
    // ============================================================
    /**
     * Mappe une entité {@link Subject} vers un {@link SubjectDTO} en incluant
     * le statut d'abonnement de l'utilisateur courant.
     *
     * @param subject sujet à mapper
     * @param user utilisateur courant
     * @return {@link SubjectDTO} correspondant au sujet
     */
    private SubjectDTO mapToDTO(Subject subject, User user) {
        boolean subscribed = subscriptionRepository.existsByUserAndSubject(user, subject);

        return new SubjectDTO(
                subject.getSubjectId(),
                subject.getName(),
                subject.getDescription(),
                subscribed
        );
    }

    // ============================================================
    // UTILS
    // ============================================================
    /**
     * Récupère un sujet par son identifiant ou lève une exception si inexistant.
     *
     * @param id identifiant du sujet
     * @return {@link Subject} correspondant à l'identifiant
     * @throws ResourceNotFoundException si le sujet n'existe pas
     */
    private Subject getSubjectOrThrow(Integer id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sujet introuvable : " + id));
    }
}
