package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
/**
 * Utilitaires pour la création, l'extraction et la validation des tokens JWT.
 *
 * Cette classe permet de générer un token JWT pour un utilisateur, d'extraire
 * les informations stockées dans le token (userId et login) et de vérifier
 * sa validité.
 *
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    /**
     * Récupère la clé secrète utilisée pour signer les JWT.
     *
     * @return la clé secrète {@link Key} pour signer les tokens
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // 🔥 la bonne signature
    /**
     * Génère un token JWT pour un utilisateur donné.
     *
     * @param login l'identifiant de connexion de l'utilisateur (email ou username)
     * @param user  l'utilisateur pour lequel générer le token
     * @return un token JWT signé
     */
    public String generateToken(String login, User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .claim("login", login) // email OU username
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Récupère les informations (claims) contenues dans un token JWT.
     *
     * @param token le token JWT
     * @return les claims du token
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrait l'ID utilisateur à partir d'un token JWT.
     *
     * @param token le token JWT
     * @return l'ID utilisateur {@link Integer}
     */
    public Integer extractUserId(String token) {
        return Integer.valueOf(getClaims(token).getSubject());
    }

    /**
     * Extrait le login (email ou username) à partir d'un token JWT.
     *
     * @param token le token JWT
     * @return le login de l'utilisateur
     */
    public String extractLogin(String token) {
        return getClaims(token).get("login", String.class);
    }

    /**
     * Vérifie si un token JWT est valide.
     *
     * La validation inclut la vérification de la signature et de l'intégrité du token.
     *
     *
     * @param token le token JWT
     * @return true si le token est valide, false sinon
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
