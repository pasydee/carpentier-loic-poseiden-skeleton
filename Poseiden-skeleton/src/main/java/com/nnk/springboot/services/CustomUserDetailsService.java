package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implémentation personnalisée de {@link UserDetailsService} utilisée par Spring Security
 * pour charger un utilisateur lors du processus d'authentification.
 *
 * <p>Cette classe interroge {@link UserService} pour récupérer un utilisateur
 * depuis la base de données à partir de son nom d'utilisateur. Si aucun utilisateur
 * n'est trouvé, une {@link UsernameNotFoundException} est levée, empêchant la connexion.</p>
 *
 * <p>Une fois l'utilisateur trouvé, cette classe construit un objet
 * {@link org.springframework.security.core.userdetails.User} compatible avec Spring Security,
 * contenant :</p>
 *
 * <ul>
 *     <li>le nom d'utilisateur</li>
 *     <li>le mot de passe encodé</li>
 *     <li>le rôle de l'utilisateur (ADMIN ou USER), automatiquement converti en ROLE_ADMIN / ROLE_USER</li>
 * </ul>
 *
 * <p>Cette classe est essentielle au mécanisme d'authentification basé sur session
 * imposé par le projet Poseidon.</p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Charge un utilisateur à partir de son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur fourni lors de la connexion
     * @return un objet {@link UserDetails} contenant les informations de sécurité
     * @throws UsernameNotFoundException si aucun utilisateur ne correspond au nom fourni
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()) // ex: "ADMIN" ou "USER"
                .build();
    }
}
