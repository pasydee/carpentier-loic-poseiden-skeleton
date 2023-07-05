package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceImplTest {

    @Mock
    private RatingRepository repository;

    @InjectMocks
    private RatingServiceImpl service;

    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("Aaa");
        rating.setSandPRating("AAA");
        rating.setFitchRating("AAA");
        rating.setOrderNumber(1);
    }

    @Test
    void findAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(rating));

        List<Rating> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void findById_shouldReturnRating() {
        when(repository.findById(1)).thenReturn(Optional.of(rating));

        Rating result = service.findById(1);

        assertEquals("Aaa", result.getMoodysRating());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(1));
    }

    @Test
    void save_shouldSaveRating() {
        when(repository.save(rating)).thenReturn(rating);

        Rating result = service.save(rating);

        assertEquals("Aaa", result.getMoodysRating());
        verify(repository).save(rating);
    }

    @Test
    void update_shouldUpdateRating() {
        when(repository.findById(1)).thenReturn(Optional.of(rating));
        when(repository.save(any(Rating.class))).thenReturn(rating);

        Rating updated = new Rating();
        updated.setMoodysRating("Updated");
        updated.setSandPRating("BBB");
        updated.setFitchRating("CCC");
        updated.setOrderNumber(2);

        Rating result = service.update(1, updated);

        assertEquals("Updated", result.getMoodysRating());
        assertEquals("BBB", result.getSandPRating());
        assertEquals("CCC", result.getFitchRating());
        assertEquals(2, result.getOrderNumber());

        verify(repository).save(any(Rating.class));
    }

    @Test
    void delete_shouldRemoveRating() {
        when(repository.findById(1)).thenReturn(Optional.of(rating));

        service.delete(1);

        verify(repository).delete(rating);
    }
}
