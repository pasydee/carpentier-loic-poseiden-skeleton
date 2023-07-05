package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;

import java.util.List;

/**
 * Service métier dédié à la gestion des utilisateurs de l'application Poseidon.
 *
 * <p>
 * Cette interface définit l'ensemble des opérations nécessaires pour manipuler
 * les entités {@link User}, incluant la création, la mise à jour, la suppression
 * et la recherche d'utilisateurs.
 * </p>
 *
 * <h2>Responsabilités principales</h2>
 *
 * <ul>
 *     <li>récupération de tous les utilisateurs</li>
 *     <li>recherche d'un utilisateur par identifiant</li>
 *     <li>création d'un utilisateur avec validation et encodage du mot de passe</li>
 *     <li>mise à jour d'un utilisateur existant (mot de passe optionnel)</li>
 *     <li>suppression d'un utilisateur</li>
 *     <li>recherche d'un utilisateur par nom d'utilisateur (utilisé pour l'authentification)</li>
 * </ul>
 *
 * <p>
 * Cette interface est implémentée par {@code UserServiceImpl}, qui contient
 * l'intégralité de la logique métier, notamment :
 * </p>
 *
 * <ul>
 *     <li>la validation de la complexité du mot de passe</li>
 *     <li>l'encodage sécurisé via {@code PasswordEncoder}</li>
 *     <li>la gestion du mot de passe optionnel lors de la mise à jour</li>
 * </ul>
 *
 * <p>
 * Elle est également utilisée par Spring Security via {@code CustomUserDetailsService}
 * pour charger un utilisateur lors de l'authentification.
 * </p>
 */
public interface UserService {

    /**
     * Retourne la liste complète des utilisateurs enregistrés.
     *
     * @return une liste d'objets {@link User}
     */
    List<User> findAll();

    /**
     * Recherche un utilisateur par son identifiant unique.
     *
     * @param id identifiant de l'utilisateur
     * @return l'utilisateur correspondant
     * @throws IllegalArgumentException si aucun utilisateur n'existe pour cet identifiant
     */
    User findById(Integer id);

    /**
     * Crée un nouvel utilisateur après validation métier et encodage du mot de passe.
     *
     * <p>
     * Le mot de passe doit respecter les règles de sécurité définies dans
     * l'implémentation du service. Une exception est levée en cas de non-conformité.
     * </p>
     *
     * @param user l'utilisateur à enregistrer
     * @return l'utilisateur sauvegardé
     * @throws IllegalArgumentException si le mot de passe ne respecte pas les règles de sécurité
     */
    User save(User user);

    /**
     * Met à jour les informations d'un utilisateur existant.
     *
     * <p>
     * Le mot de passe est optionnel : s'il est vide ou nul, l'ancien mot de passe est conservé.
     * S'il est renseigné, il doit respecter les règles de sécurité et sera encodé avant sauvegarde.
     * </p>
     *
     * @param id   identifiant de l'utilisateur à mettre à jour
     * @param user données mises à jour
     * @return l'utilisateur mis à jour
     * @throws IllegalArgumentException si l'utilisateur n'existe pas
     *                                  ou si le nouveau mot de passe est invalide
     */
    User update(Integer id, User user);

    /**
     * Supprime un utilisateur à partir de son identifiant.
     *
     * @param id identifiant de l'utilisateur à supprimer
     * @throws IllegalArgumentException si l'utilisateur n'existe pas
     */
    void delete(Integer id);

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * <p>
     * Cette méthode est utilisée par Spring Security lors de l'authentification.
     * </p>
     *
     * @param username nom d'utilisateur
     * @return l'utilisateur correspondant ou {@code null} s'il n'existe pas
     */
    User findByUsername(String username);
}
