package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurvePointServiceImplTest {

    @Mock
    private CurvePointRepository repository;

    @InjectMocks
    private CurvePointServiceImpl service;

    private CurvePoint curve;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        curve = new CurvePoint();
        curve.setId(1);
        curve.setCurveId(10);
        curve.setTerm(BigDecimal.valueOf(20));
        curve.setValue(BigDecimal.valueOf(30));
    }

    @Test
    void findAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(curve));

        List<CurvePoint> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void findById_shouldReturnCurvePoint() {
        when(repository.findById(1)).thenReturn(Optional.of(curve));

        CurvePoint result = service.findById(1);

        assertEquals(10, result.getCurveId());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(1));
    }

    @Test
    void save_shouldSaveCurvePoint() {
        when(repository.save(curve)).thenReturn(curve);

        CurvePoint result = service.save(curve);

        assertEquals(10, result.getCurveId());
        verify(repository).save(curve);
    }

    @Test
    void update_shouldUpdateCurvePoint() {
        when(repository.findById(1)).thenReturn(Optional.of(curve));
        when(repository.save(any(CurvePoint.class))).thenReturn(curve);

        CurvePoint updated = new CurvePoint();
        updated.setCurveId(99);
        updated.setTerm(BigDecimal.valueOf(50));
        updated.setValue(BigDecimal.valueOf(60));

        CurvePoint result = service.update(1, updated);

        assertEquals(99, result.getCurveId());
        assertEquals(BigDecimal.valueOf(50), result.getTerm());
        assertEquals(BigDecimal.valueOf(60), result.getValue());

        verify(repository).save(any(CurvePoint.class));
    }

    @Test
    void delete_shouldRemoveCurvePoint() {
        when(repository.findById(1)).thenReturn(Optional.of(curve));

        service.delete(1);

        verify(repository).delete(curve);
    }
}
