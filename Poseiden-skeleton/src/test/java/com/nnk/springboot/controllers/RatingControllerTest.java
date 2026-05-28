package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    // -----------------------------
    // GET /rating/list
    // -----------------------------
    @Test
    void testGetRatingList() throws Exception {
        when(ratingService.findAll()).thenReturn(List.of(new Rating()));

        mockMvc.perform(get("/rating/list")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"));
    }

    // -----------------------------
    // GET /rating/add
    // -----------------------------
    @Test
    void testAddRatingForm() throws Exception {
        mockMvc.perform(get("/rating/add")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    // -----------------------------
    // POST /rating/validate
    // -----------------------------
    @Test
    void testValidateRating() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .param("moodysRating", "Aaa")
                        .param("sandPRating", "AAA")
                        .param("fitchRating", "AAA")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).save(any(Rating.class));
    }

    // -----------------------------
    // GET /rating/update/{id}
    // -----------------------------
    @Test
    void testShowUpdateForm() throws Exception {
        Rating rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("Aaa");
        rating.setSandPRating("AAA");
        rating.setFitchRating("AAA");
        rating.setOrderNumber(1);

        when(ratingService.findById(1)).thenReturn(rating);

        mockMvc.perform(get("/rating/update/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));
    }

    // -----------------------------
    // POST /rating/update/{id}
    // -----------------------------
    @Test
    void testUpdateRating() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .param("moodysRating", "Baa")
                        .param("sandPRating", "BBB")
                        .param("fitchRating", "BBB")
                        .param("orderNumber", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).update(eq(1), any(Rating.class));
    }

    // -----------------------------
    // GET /rating/delete/{id}
    // -----------------------------
    @Test
    void testDeleteRating() throws Exception {
        mockMvc.perform(get("/rating/delete/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).delete(1);
    }
}
