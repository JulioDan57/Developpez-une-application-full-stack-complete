package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        CustomUserDetails cud = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findById(cud.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }
}
