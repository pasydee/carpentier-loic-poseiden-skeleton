package com.nnk.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration centrale de Spring Security pour l'application Poseidon.
 *
 * <p>
 * Cette classe définit l'ensemble des règles de sécurité applicables à l'application,
 * notamment la gestion de l'authentification, des autorisations, du login personnalisé
 * et du mécanisme de déconnexion.
 * </p>
 *
 * <h2>Fonctionnalités principales</h2>
 *
 * <ul>
 *     <li><strong>Gestion des ressources publiques</strong> :
 *         les fichiers statiques (CSS, JS) ainsi que la page de login sont accessibles
 *         sans authentification.</li>
 *
 *     <li><strong>Protection des routes sensibles</strong> :
 *         toutes les routes sous <code>/user/**</code> sont réservées au rôle ADMIN.</li>
 *
 *     <li><strong>Authentification via formulaire</strong> :
 *         utilisation d'une page de login personnalisée (<code>/login</code>),
 *         avec des paramètres de formulaire adaptés (<code>email</code> et <code>password</code>).</li>
 *
 *     <li><strong>Redirection après connexion</strong> :
 *         un {@link CustomSuccessHandler} détermine dynamiquement la page de redirection
 *         en fonction du rôle de l'utilisateur.</li>
 *
 *     <li><strong>Déconnexion sécurisée</strong> :
 *         la déconnexion est autorisée via une requête GET sur <code>/logout</code>
 *         grâce à un {@link org.springframework.security.web.util.matcher.AntPathRequestMatcher}.
 *         La session est invalidée, le cookie JSESSIONID supprimé, puis l'utilisateur
 *         est redirigé vers <code>/login?logout</code>.</li>
 *
 *     <li><strong>Encodage des mots de passe</strong> :
 *         un bean {@link BCryptPasswordEncoder} est exposé pour garantir un stockage
 *         sécurisé des mots de passe.</li>
 *
 *     <li><strong>Gestion de l'AuthenticationManager</strong> :
 *         l'AuthenticationManager fourni par Spring est exposé pour permettre
 *         l'authentification des utilisateurs via le service dédié.</li>
 * </ul>
 *
 * <p>
 * Cette configuration repose sur une authentification basée sur session,
 * conformément aux exigences du projet Poseidon.
 * </p>
 */
@Configuration
public class SecurityConfig {

    private final CustomSuccessHandler successHandler;

    /**
     * Constructeur injectant le gestionnaire de redirection post-authentification.
     *
     * @param successHandler gestionnaire de redirection selon le rôle utilisateur
     */
    public SecurityConfig(CustomSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    /**
     * Déclare le bean BCrypt utilisé pour encoder les mots de passe
     * avant leur stockage en base de données.
     *
     * @return un encodeur BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Définit la chaîne de filtres de sécurité Spring Security.
     *
     * @param http l'objet HttpSecurity configuré par Spring
     * @return la chaîne de filtres de sécurité
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/user/add", "/user/validate").permitAll()
                        .requestMatchers("/user/**").hasRole("ADMIN")
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(
                                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/logout", "GET")
                        )
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Expose l'AuthenticationManager utilisé par Spring Security
     * pour gérer l'authentification des utilisateurs.
     *
     * @param authConfig configuration d'authentification fournie par Spring
     * @return l'AuthenticationManager configuré
     * @throws Exception en cas d'erreur
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
