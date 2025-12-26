package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtUtils;
import com.openclassrooms.mddapi.security.SecurityUtils;
import com.openclassrooms.mddapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Contrôleur REST pour l'authentification et la gestion du profil utilisateur.
 * <p>
 * Ce contrôleur gère l'inscription, la connexion, la récupération des informations de l'utilisateur
 * connecté ainsi que la mise à jour de son profil. Les routes sont sécurisées par JWT, sauf pour
 * l'inscription et la connexion.
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final SecurityUtils securityUtils;

    /**
     * Crée un nouvel utilisateur.
     *
     * @param request DTO {@link RegisterRequest} contenant email, username et mot de passe
     * @return ResponseEntity contenant un {@link RegisterResponse} avec le JWT généré
     */
    @Operation(summary = "Créer un nouvel utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Authentifie un utilisateur avec son email ou username et son mot de passe.
     *
     * @param request DTO {@link AuthRequest} contenant email/username et mot de passe
     * @return ResponseEntity contenant {@link AuthResponse} avec le JWT
     */
    @Operation(summary = "Se connecter avec email ou username et mot de passe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Récupère les informations de l'utilisateur actuellement connecté.
     *
     * @return ResponseEntity contenant {@link AuthUserProfileResponse} avec les informations du profil
     */
    @Operation(summary = "Récupérer les informations de l'utilisateur connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Informations récupérées"),
            @ApiResponse(responseCode = "401", description = "Token manquant ou invalide")
    })
    @GetMapping("/me")
    public ResponseEntity<AuthUserProfileResponse> me() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

    /**
     * Met à jour les informations du profil de l'utilisateur connecté.
     *
     * @param request DTO {@link AuthUserUpdateRequest} contenant les nouvelles informations à modifier
     * @return ResponseEntity contenant {@link AuthUserUpdateResponse} avec le nouveau JWT mis à jour
     * @throws IOException en cas de problème lors du traitement de l'update (ex: image de profil)
     */
    @Operation(summary = "modifier les parametres de l'utilisateur connecté")
    @PutMapping("/me")
    public ResponseEntity<AuthUserUpdateResponse> update(@Valid @RequestBody AuthUserUpdateRequest request
    ) throws IOException {
        return ResponseEntity.ok(authService.update(request));
    }
}

