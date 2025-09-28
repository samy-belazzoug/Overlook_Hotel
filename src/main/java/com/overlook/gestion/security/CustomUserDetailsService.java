package com.overlook.gestion.security;

import com.overlook.gestion.domain.Gestionnaire;
import com.overlook.gestion.repository.GestionnaireRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final GestionnaireRepository repository;

    public CustomUserDetailsService(GestionnaireRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Gestionnaire g = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + email));
        return new CustomUserDetails(g);
    }
}
