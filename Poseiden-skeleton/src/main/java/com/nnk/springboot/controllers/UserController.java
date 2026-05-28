package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur MVC responsable de la gestion des utilisateurs via l'interface web.
 *
 * <p>
 * Toutes les routes de ce contrôleur sont protégées par Spring Security et
 * accessibles uniquement au rôle <strong>ADMIN</strong>, à l’exception de
 * l’ajout du premier utilisateur (géré dans {@code SecurityConfig}).
 * </p>
 *
 * <h2>Fonctionnalités principales</h2>
 *
 * <ul>
 *     <li>Affichage de la liste des utilisateurs</li>
 *     <li>Création d’un nouvel utilisateur</li>
 *     <li>Validation et enregistrement d’un utilisateur</li>
 *     <li>Affichage du formulaire de mise à jour</li>
 *     <li>Mise à jour d’un utilisateur existant</li>
 *     <li>Suppression d’un utilisateur</li>
 * </ul>
 *
 * <h2>Gestion du mot de passe</h2>
 *
 * <ul>
 *     <li>Lors de la création : le mot de passe est obligatoire et validé</li>
 *     <li>Lors de la mise à jour : le mot de passe est optionnel</li>
 *     <li>Si un nouveau mot de passe est fourni, il doit respecter les règles
 *         de sécurité définies dans {@link com.nnk.springboot.services.UserServiceImpl#validatePassword(String)}</li>
 *     <li>L’encodage du mot de passe est effectué exclusivement dans le service,
 *         garantissant une architecture MVC propre</li>
 * </ul>
 *
 * <p>
 * Ce contrôleur délègue toute la logique métier à {@link UserService},
 * notamment la validation métier, la gestion du mot de passe et les opérations CRUD.
 * </p>
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructeur injectant le service utilisateur.
     *
     * @param userService service gérant la logique métier liée aux utilisateurs
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Affiche la liste complète des utilisateurs.
     *
     * @param model modèle contenant la liste des utilisateurs
     * @return la vue "user/list"
     */
    @GetMapping("/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouvel utilisateur.
     *
     * @param user objet utilisateur vide injecté dans le formulaire
     * @return la vue "user/add"
     */
    @GetMapping("/add")
    public String addUserForm(User user) {
        return "user/add";
    }

    /**
     * Valide et enregistre un nouvel utilisateur.
     *
     * <p>
     * La validation Bean Validation est appliquée sur les champs obligatoires.
     * La validation métier du mot de passe est effectuée dans {@link UserService#save(User)}.
     * </p>
     *
     * @param user   utilisateur à valider
     * @param result résultat de la validation
     * @param model  modèle pour la vue
     * @return redirection vers la liste des utilisateurs ou retour au formulaire en cas d'erreur
     */
    @PostMapping("/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {

        try {
            userService.save(user);
        } catch (IllegalArgumentException e) {
            result.rejectValue("password", "error.user", e.getMessage());
        }

        if (result.hasErrors()) {
            return "user/add";
        }

        return "redirect:/user/list";
    }

    /**
     * Affiche le formulaire de mise à jour d'un utilisateur existant.
     *
     * <p>
     * Le mot de passe est volontairement vidé afin de ne jamais afficher le hash
     * en clair dans le formulaire.
     * </p>
     *
     * @param id    identifiant de l'utilisateur à modifier
     * @param model modèle contenant l'utilisateur à afficher
     * @return la vue "user/update"
     */
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id);
        user.setPassword(""); // ne jamais afficher le hash
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * Met à jour un utilisateur après validation.
     *
     * <p>
     * Le mot de passe est optionnel : s'il est vide, l'ancien mot de passe est conservé.
     * S'il est renseigné, il doit respecter les règles de sécurité.
     * Toute la logique métier est déléguée à {@link UserService#update(Integer, User)}.
     * </p>
     *
     * @param id     identifiant de l'utilisateur
     * @param user   données mises à jour
     * @param result résultat de la validation
     * @param model  modèle pour la vue
     * @return redirection vers la liste ou retour au formulaire en cas d'erreur
     */
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @Valid User user,
                             BindingResult result,
                             Model model) {

        try {
            userService.update(id, user);
        } catch (IllegalArgumentException e) {
            result.rejectValue("password", "error.user", e.getMessage());
        }

        if (result.hasErrors()) {
            return "user/update";
        }

        return "redirect:/user/list";
    }

    /**
     * Supprime un utilisateur à partir de son identifiant.
     *
     * @param id identifiant de l'utilisateur à supprimer
     * @return redirection vers la liste des utilisateurs
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.delete(id);
        return "redirect:/user/list";
    }
}
