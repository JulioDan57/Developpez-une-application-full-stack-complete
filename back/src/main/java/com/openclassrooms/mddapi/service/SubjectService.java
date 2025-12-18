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

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SecurityUtils securityUtils;

    // ============================================================
    // GET ALL SUBJECTS (with subscribed flag)
    // ============================================================

    public List<SubjectDTO> getAllSubjectsDTO() {
        User user = securityUtils.getCurrentUser();

        return subjectRepository.findAll().stream()
                .map(subject -> mapToDTO(subject, user))
                .collect(Collectors.toList());
    }

    // ============================================================
    // GET ONLY SUBSCRIBED SUBJECTS
    // ============================================================
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

    public void unsubscribe(Integer subjectId) {
        User user = securityUtils.getCurrentUser();
        Subject subject = getSubjectOrThrow(subjectId);

        subscriptionRepository.findByUserAndSubject(user, subject)
                .ifPresent(subscriptionRepository::delete);
    }

    // ============================================================
    // MAPPING
    // ============================================================

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
    private Subject getSubjectOrThrow(Integer id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sujet introuvable : " + id));
    }
}
