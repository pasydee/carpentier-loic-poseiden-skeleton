package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setRole("ADMIN");
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        when(userService.findByUsername("testUser")).thenReturn(user);

        UserDetails result = customUserDetailsService.loadUserByUsername("testUser");

        assertEquals("testUser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        verify(userService).findByUsername("testUser");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        when(userService.findByUsername("unknown")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("unknown"));

        verify(userService).findByUsername("unknown");
    }
}
