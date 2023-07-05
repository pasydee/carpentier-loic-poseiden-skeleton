package com.nnk.springboot.controllers;

import com.nnk.springboot.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur responsable de la gestion des pages liées à l'authentification
 * et aux accès sécurisés de l'application Poseidon.
 *
 * <p>
 * Ce contrôleur ne gère pas directement l'authentification (prise en charge par
 * Spring Security), mais fournit les vues nécessaires :
 * </p>
 *
 * <ul>
 *     <li>la page de login personnalisée</li>
 *     <li>une page sécurisée affichant des données après authentification</li>
 *     <li>la page d'erreur 403 en cas d'accès non autorisé</li>
 * </ul>
 *
 * <p>
 * La logique métier liée aux utilisateurs est déléguée à {@link UserService}.
 * </p>
 */
@Controller
public class LoginController {

    private final UserService userService;

    /**
     * Constructeur injectant le service utilisateur.
     *
     * @param userService service permettant d'accéder aux données utilisateurs
     */
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Affiche la page de login personnalisée.
     *
     * <p>
     * Cette page est utilisée par Spring Security comme point d'entrée
     * pour l'authentification, conformément à la configuration définie
     * dans {@code SecurityConfig}.
     * </p>
     *
     * @return la vue "login"
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Exemple de page sécurisée affichant une liste d'utilisateurs.
     *
     * <p>
     * Cette route est protégée par Spring Security et n'est accessible
     * qu'après authentification. Elle illustre l'accès à des données
     * sécurisées.
     * </p>
     *
     * @param model modèle contenant la liste des utilisateurs
     * @return la vue "user/list"
     */
    @GetMapping("/secure/article-details")
    public String getAllUserArticles(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    /**
     * Affiche la page d'erreur 403 lorsqu'un utilisateur tente d'accéder
     * à une ressource pour laquelle il n'a pas les autorisations nécessaires.
     *
     * @param model modèle contenant le message d'erreur
     * @return la vue "403"
     */
    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("errorMsg", "You are not authorized for the requested data.");
        return "403";
    }
}
