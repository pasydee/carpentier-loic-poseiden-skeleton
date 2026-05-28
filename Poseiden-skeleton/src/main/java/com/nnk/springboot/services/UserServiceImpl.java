package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation de {@link UserService} gérant l'ensemble de la logique métier
 * liée aux utilisateurs de l'application Poseidon.
 *
 * <p>
 * Cette classe centralise :
 * </p>
 *
 * <ul>
 *     <li>les opérations CRUD sur les utilisateurs</li>
 *     <li>la validation métier des mots de passe</li>
 *     <li>l'encodage sécurisé des mots de passe via {@link PasswordEncoder}</li>
 *     <li>la gestion du changement de mot de passe (optionnel en mise à jour)</li>
 *     <li>la recherche d'utilisateur pour Spring Security</li>
 * </ul>
 *
 * <p>
 * Elle garantit que toutes les règles de sécurité sont respectées avant toute
 * persistance en base de données.
 * </p>
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructeur injectant le repository et l'encodeur de mot de passe.
     *
     * @param repository      repository d'accès aux utilisateurs
     * @param passwordEncoder encodeur de mot de passe utilisé pour sécuriser les comptes
     */
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retourne la liste complète des utilisateurs.
     *
     * @return liste des utilisateurs
     */
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur
     * @return utilisateur correspondant
     * @throws IllegalArgumentException si aucun utilisateur n'est trouvé
     */
    @Override
    public User findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    /**
     * Crée un nouvel utilisateur après validation et encodage du mot de passe.
     *
     * <p>
     * Le mot de passe doit respecter les règles de sécurité définies dans
     * {@link #validatePassword(String)}. Une fois validé, il est encodé via
     * {@link PasswordEncoder} avant d'être sauvegardé.
     * </p>
     *
     * @param user utilisateur à enregistrer
     * @return utilisateur sauvegardé
     * @throws IllegalArgumentException si le mot de passe ne respecte pas les règles de sécurité
     */
    @Override
    public User save(User user) {

        validatePassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    /**
     * Met à jour un utilisateur existant en appliquant les règles suivantes :
     *
     * <ul>
     *     <li>les champs simples (username, fullname, role) sont toujours mis à jour</li>
     *     <li>si un nouveau mot de passe est fourni, il est validé puis encodé</li>
     *     <li>si aucun mot de passe n'est fourni, l'ancien mot de passe est conservé</li>
     *     <li>l'identifiant de l'utilisateur n'est jamais modifié</li>
     * </ul>
     *
     * <p>
     * Cette méthode garantit que les informations sensibles, notamment le mot de passe,
     * respectent les règles de sécurité définies par {@link #validatePassword(String)}.
     * </p>
     *
     * @param id    identifiant de l'utilisateur à mettre à jour
     * @param user  données mises à jour provenant du formulaire
     * @return      l'utilisateur mis à jour et sauvegardé en base
     *
     * @throws IllegalArgumentException si l'utilisateur n'existe pas
     *                                  ou si le nouveau mot de passe ne respecte pas les règles de sécurité
     */
    @Override
    public User update(Integer id, User user) {
        User existing = findById(id);

        existing.setUsername(user.getUsername());
        existing.setFullname(user.getFullname());
        existing.setRole(user.getRole());

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            validatePassword(user.getPassword());
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return repository.save(existing);
    }

    /**
     * Supprime un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur à supprimer
     * @throws IllegalArgumentException si l'utilisateur n'existe pas
     */
    @Override
    public void delete(Integer id) {
        repository.delete(findById(id));
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * <p>
     * Cette méthode est utilisée par Spring Security lors de l'authentification.
     * </p>
     *
     * @param username nom d'utilisateur
     * @return utilisateur correspondant ou {@code null} s'il n'existe pas
     */
    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    /**
     * Valide la complexité du mot de passe selon les règles de sécurité :
     *
     * <ul>
     *     <li>au moins 8 caractères</li>
     *     <li>au moins une majuscule</li>
     *     <li>au moins un chiffre</li>
     *     <li>au moins un symbole parmi {@code @$!%*?&}</li>
     * </ul>
     *
     * @param password mot de passe à valider
     * @throws IllegalArgumentException si le mot de passe ne respecte pas les règles
     */
    private void validatePassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&]).{8,}$";

        if (password == null || !password.matches(regex)) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters, one uppercase letter, one number, and one symbol."
            );
        }
    }
}
