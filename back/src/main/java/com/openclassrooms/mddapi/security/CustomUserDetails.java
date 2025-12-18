package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Integer userId;
    private final String email;
    private final String username;   // <-- Ajout important
    private final String password;
    private final String loginUsed; // email OU username

    public CustomUserDetails(User user,  String loginUsed) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.username = user.getUsername(); // <-- ici aussi
        this.password = user.getPassword();
        this.loginUsed = loginUsed;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return loginUsed;
    }

}

