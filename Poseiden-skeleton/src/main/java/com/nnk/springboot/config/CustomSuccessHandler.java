package com.nnk.springboot.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Gestionnaire personnalisé de succès d'authentification.
 *
 * <p>
 * Cette classe est invoquée automatiquement par Spring Security
 * lorsqu'un utilisateur s'authentifie avec succès.
 * Elle permet de remplacer le comportement par défaut de Spring Security
 * (qui redirige toujours vers la page initialement demandée)
 * par une logique métier personnalisée basée sur le rôle de l'utilisateur.
 * </p>
 *
 * <h2>Fonctionnement</h2>
 *
 * <ul>
 *     <li>Si l'utilisateur possède le rôle <strong>ADMIN</strong>,
 *         il est redirigé vers la page d'accueil (<code>/</code>).</li>
 *
 *     <li>Si l'utilisateur possède le rôle <strong>USER</strong>,
 *         il est redirigé vers la liste des BidList
 *         (<code>/bidList/list</code>).</li>
 *
 *     <li>Si aucun rôle reconnu n'est trouvé,
 *         l'utilisateur est renvoyé vers la page de login.</li>
 * </ul>
 *
 * <p>
 * Ce gestionnaire est déclaré comme bean Spring via {@link Component}
 * et est injecté dans {@link SecurityConfig} pour remplacer le
 * comportement standard de redirection après connexion.
 * </p>
 */
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Méthode appelée automatiquement après une authentification réussie.
     *
     * @param request        requête HTTP
     * @param response       réponse HTTP
     * @param authentication objet contenant les informations de l'utilisateur authentifié
     * @throws IOException      en cas d'erreur d'écriture dans la réponse
     * @throws ServletException en cas d'erreur liée au traitement de la requête
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Si ADMIN → redirection vers /
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.sendRedirect("/");
            return;
        }

        // Si USER → redirection vers /bidList/list
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            response.sendRedirect("/bidList/list");
            return;
        }

        // Par défaut → retour au login
        response.sendRedirect("/login");
    }
}
