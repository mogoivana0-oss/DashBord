package com.recycling.security;

import com.recycling.model.User;
import com.recycling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service pour charger les détails de l'utilisateur depuis la base de données
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur non trouvé avec le nom d'utilisateur: " + username));
        
        if (!user.isEnabled()) {
            throw new UsernameNotFoundException("Compte utilisateur désactivé");
        }
        
        if (user.isAccountNonLocked()) {
            return user;
        } else {
            throw new UsernameNotFoundException("Compte utilisateur verrouillé");
        }
    }
}
