package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Entité représentant un utilisateur de l'application Poseidon.
 *
 * <p>
 * Cette classe correspond à la table <code>users</code> en base de données et
 * contient les informations nécessaires à l'authentification et à la gestion
 * des comptes utilisateurs.
 * </p>
 *
 * <h2>Règles de validation</h2>
 * <ul>
 *     <li><strong>username</strong> : obligatoire</li>
 *     <li><strong>fullname</strong> : obligatoire</li>
 *     <li><strong>role</strong> : obligatoire</li>
 *     <li><strong>password</strong> : non obligatoire ici, car la validation
 *         métier est gérée dans le service :
 *         <ul>
 *             <li>obligatoire en création</li>
 *             <li>optionnel en mise à jour</li>
 *             <li>validé et encodé dans {@code UserServiceImpl}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <p>
 * Le mot de passe n'est volontairement pas annoté avec {@code @NotBlank} afin
 * de permettre une mise à jour sans changement de mot de passe.
 * </p>
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Identifiant unique de l'utilisateur.
     * Généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nom d'utilisateur utilisé pour la connexion.
     * Ce champ est obligatoire.
     */
    @NotBlank(message = "Username is mandatory")
    private String username;

    /**
     * Mot de passe de l'utilisateur.
     *
     * <p>
     * Ce champ n'est pas annoté avec {@code @NotBlank} car :
     * </p>
     * <ul>
     *     <li>il est obligatoire uniquement lors de la création</li>
     *     <li>il est optionnel lors de la mise à jour</li>
     *     <li>la validation métier est effectuée dans {@code UserServiceImpl}</li>
     * </ul>
     */
    private String password;

    /**
     * Nom complet de l'utilisateur.
     * Ce champ est obligatoire.
     */
    @NotBlank(message = "FullName is mandatory")
    private String fullname;

    /**
     * Rôle de l'utilisateur (ex : ROLE_ADMIN, ROLE_USER).
     * Ce champ est obligatoire.
     */
    @NotBlank(message = "Role is mandatory")
    private String role;

    // ----- Getters & Setters -----

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
