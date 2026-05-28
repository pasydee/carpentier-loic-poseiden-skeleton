package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setFullname("Test User");
        user.setRole("USER");
        user.setPassword("encodedPassword");
    }

    @Test
    void findAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(user));

        List<User> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void findById_shouldReturnUser() {
        when(repository.findById(1)).thenReturn(Optional.of(user));

        User result = service.findById(1);

        assertEquals("testUser", result.getUsername());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(1));
    }

    @Test
    void save_shouldEncodePasswordAndSaveUser() {
        when(passwordEncoder.encode("Valid1!Pass")).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(user);

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setFullname("New User");
        newUser.setRole("ADMIN");
        newUser.setPassword("Valid1!Pass"); // ✔ Mot de passe valide

        User result = service.save(newUser);

        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder).encode("Valid1!Pass");
        verify(repository).save(any(User.class));
    }



    @Test
    void update_shouldUpdateUserWithoutChangingPasswordIfNull() {
        when(repository.findById(1)).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);

        User updated = new User();
        updated.setUsername("updatedUser");
        updated.setFullname("Updated User");
        updated.setRole("ADMIN");
        updated.setPassword(null); // pas de changement de mot de passe

        User result = service.update(1, updated);

        assertEquals("updatedUser", result.getUsername());
        assertEquals("Updated User", result.getFullname());
        assertEquals("ADMIN", result.getRole());
        assertEquals("encodedPassword", result.getPassword()); // inchangé

        verify(repository).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void update_shouldEncodePasswordIfProvided() {
        when(repository.findById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("Valid1!Pass")).thenReturn("encodedValid1!Pass");
        when(repository.save(any(User.class))).thenReturn(user);

        User updated = new User();
        updated.setUsername("updatedUser");
        updated.setFullname("Updated User");
        updated.setRole("ADMIN");
        updated.setPassword("Valid1!Pass");

        User result = service.update(1, updated);

        assertEquals("encodedValid1!Pass", result.getPassword());
        verify(passwordEncoder).encode("Valid1!Pass");
        verify(repository).save(any(User.class));
    }

    @Test
    void delete_shouldRemoveUser() {
        when(repository.findById(1)).thenReturn(Optional.of(user));

        service.delete(1);

        verify(repository).delete(user);
    }
}
