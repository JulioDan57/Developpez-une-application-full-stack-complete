package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.exception.ConflictException;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtUtils;
import com.openclassrooms.mddapi.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service gérant la logique métier liée à l'authentification,
 * la gestion des utilisateurs et la génération des tokens JWT.
 *
 * Ce service fournit les fonctionnalités suivantes :
 * <ul>
 *     <li>Inscription d'un nouvel utilisateur</li>
 *     <li>Connexion et génération de JWT</li>
 *     <li>Récupération du profil de l'utilisateur connecté</li>
 *     <li>Mise à jour du profil et du mot de passe</li>
 *     <li>Validation des mots de passe</li>
 * </ul>
 *
 */
@Service
public class AuthService {


    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private SecurityUtils securityUtils;

    /**
     * Constructeur pour l'injection des dépendances essentielles.
     *
     * @param userRepository repository pour gérer les utilisateurs
     * @param passwordEncoder encodeur de mots de passe
     * @param jwtUtils utilitaire pour la génération et validation de JWT
     */
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Enregistre un nouvel utilisateur et génère un token JWT.
     *
     * @param request données de l'utilisateur à créer
     * @return {@link RegisterResponse} contenant le JWT
     * @throws ConflictException si l'email ou le username est déjà utilisé
     */
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail()) ||
            userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Utilisateur déjà existant");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword()) // mot de passe **non encodé**
                .build();

        // 1️⃣ Validation du mot de passe
        if (!isValidPassword(user.getPassword())) {
            throw new ConflictException("Mot de passe invalide : minimum 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.");
        }

        // 2️⃣ Encodage du mot de passe après validation
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3️⃣ Sauvegarde de l'utilisateur
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail(), user);
        return new RegisterResponse(token);
    }

    /**
     * Authentifie un utilisateur existant et génère un token JWT.
     *
     * @param request informations de connexion (email/username + mot de passe)
     * @return {@link AuthResponse} contenant le JWT
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    public AuthResponse login(AuthRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmailOrUsername(
                request.getUsernameOrEmail(),
                request.getUsernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + request.getUsernameOrEmail()));

        // Génération du token
        String token = jwtUtils.generateToken(user.getEmail(), user);
        return new AuthResponse(token);
    }

    /**
     * Récupère les informations du profil de l'utilisateur connecté.
     *
     * @return {@link AuthUserProfileResponse} contenant les informations du profil
     */
    public AuthUserProfileResponse getCurrentUser() {
        User user = securityUtils.getCurrentUser();

        AuthUserProfileResponse dto = new AuthUserProfileResponse();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setSubscriptions(user.getSubscriptions().stream()
                .map(sub -> new SubscriptionDTO(
                        sub.getSubscriptionId(),
                        sub.getSubject().getSubjectId(),
                        sub.getSubject().getName(),
                        sub.getSubject().getDescription()))
                .collect(Collectors.toList()));

        return dto;
    }

    /**
     * Met à jour les informations de l'utilisateur connecté.
     *
     * Peut inclure la modification de l'email, du username et du mot de passe.
     * Un nouveau token JWT est généré après mise à jour.
     *
     *
     * @param request informations à mettre à jour
     * @return {@link AuthUserUpdateResponse} contenant le nouveau token
     * @throws ConflictException si email ou username est déjà utilisé
     */
    public AuthUserUpdateResponse update(AuthUserUpdateRequest request) {
        User user = securityUtils.getCurrentUser();

        // Vérification email et username déjà existants chez un autre utilisateur
        if (request.getEmail() != null &&
                userRepository.existsByEmail(request.getEmail()) &&
                !user.getEmail().equals(request.getEmail())) {
            throw new ConflictException("Email déjà utilisé");
        }

        if (request.getUsername() != null &&
                userRepository.existsByUsername(request.getUsername()) &&
                !user.getUsername().equals(request.getUsername())) {
            throw new ConflictException("Nom d'utilisateur déjà utilisé");
        }

        // 1️⃣ Validation du mot de passe **seulement si demandé**
        if (request.getPassword() != null && !request.getPassword().isBlank()) {

            if (!isValidPassword(request.getPassword())) {
                throw new ConflictException(
                        "Mot de passe invalide : minimum 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial."
                );
            }

            // 2️⃣ Encodage après validation
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Mise à jour email et username
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getUsername() != null) user.setUsername(request.getUsername());

        userRepository.save(user);

        // 3️⃣ On renvoie un nouveau token mis à jour
        String token = jwtUtils.generateToken(user.getEmail(), user);

        return new AuthUserUpdateResponse(token);
    }

    /**
     * Récupère un utilisateur à partir d'un token JWT.
     *
     * @param token JWT de l'utilisateur
     * @return {@link User} correspondant
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    public User getUserFromToken(String token) {
        Integer userId = jwtUtils.extractUserId(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + userId));
    }

    /**
     * Met à jour le profil d'un utilisateur existant.
     *
     * @param user utilisateur à mettre à jour
     * @return {@link User} mis à jour
     */
    public User updateProfile(User user) {
        return userRepository.save(user);
    }

    /**
     * Récupère la liste de tous les utilisateurs.
     *
     * @return liste des {@link User} existants
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Valide un mot de passe selon les règles de sécurité :
     * minimum 8 caractères, au moins une majuscule, une minuscule,
     * un chiffre et un caractère spécial.
     *
     * @param password mot de passe à valider
     * @return true si le mot de passe est valide, false sinon
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        // Expression régulière : minuscule, majuscule, chiffre, caractère spécial
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";
        return password.matches(pattern);
    }
}
