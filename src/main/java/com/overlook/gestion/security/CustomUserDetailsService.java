package com.overlook.gestion.security;

import com.overlook.gestion.domain.Gestionnaire;
import com.overlook.gestion.domain.User;
import com.overlook.gestion.repository.GestionnaireRepository;
import com.overlook.gestion.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final GestionnaireRepository gestionnaireRepository;
    private final UserRepository userRepository;

    public CustomUserDetailsService(GestionnaireRepository gestionnaireRepository, UserRepository userRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Chercher d'abord dans la table users
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return new CustomUserDetails(user);
        }
        
        // Si pas trouvé, chercher dans la table gestionnaires
        Gestionnaire gestionnaire = gestionnaireRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + email));
        return new CustomUserDetails(gestionnaire);
    }
}
