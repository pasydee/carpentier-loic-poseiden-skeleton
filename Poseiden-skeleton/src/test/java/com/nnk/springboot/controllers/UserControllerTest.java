package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    // -----------------------------
    // GET /user/list
    // -----------------------------
    @Test
    void testGetUserList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/list")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"));
    }

    // -----------------------------
    // GET /user/add
    // -----------------------------
    @Test
    void testAddUserForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/add")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    // -----------------------------
    // POST /user/validate
    // -----------------------------
    @Test
    void testValidateUser() throws Exception {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "Password1!")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).save(any(User.class));
    }

    // -----------------------------
    // GET /user/update/{id}
    // -----------------------------
    @Test
    void testShowUpdateForm() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("Password1!");
        user.setRole("USER");

        when(userService.findById(1)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/update/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));
    }

    // -----------------------------
    // POST /user/update/{id}
    // -----------------------------
    @Test
    void testUpdateUser() throws Exception {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("username", "updatedUser")
                        .param("password", "Password1!")
                        .param("fullname", "Updated User")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).update(eq(1), any(User.class));
    }

    // -----------------------------
    // GET /user/delete/{id}
    // -----------------------------
    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).delete(1);
    }
}
