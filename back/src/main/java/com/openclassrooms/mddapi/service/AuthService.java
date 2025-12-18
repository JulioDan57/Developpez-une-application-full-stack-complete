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

@Service
public class AuthService {


    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private SecurityUtils securityUtils;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

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


    public User getUserFromToken(String token) {
        Integer userId = jwtUtils.extractUserId(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + userId));
    }

    public User updateProfile(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ---------------- Validation du mot de passe ----------------
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        // Expression régulière : minuscule, majuscule, chiffre, caractère spécial
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";
        return password.matches(pattern);
    }
}
