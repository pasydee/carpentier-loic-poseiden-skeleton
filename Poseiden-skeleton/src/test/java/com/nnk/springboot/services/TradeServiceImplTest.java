package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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

class TradeServiceImplTest {

    @Mock
    private TradeRepository repository;

    @InjectMocks
    private TradeServiceImpl service;

    private Trade trade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("AccountTest");
        trade.setType("TypeTest");
        trade.setBuyQuantity(BigDecimal.valueOf(10.0));
    }

    @Test
    void findAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(trade));

        List<Trade> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void findById_shouldReturnTrade() {
        when(repository.findById(1)).thenReturn(Optional.of(trade));

        Trade result = service.findById(1);

        assertEquals("AccountTest", result.getAccount());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(1));
    }

    @Test
    void save_shouldSaveTrade() {
        when(repository.save(trade)).thenReturn(trade);

        Trade result = service.save(trade);

        assertEquals("AccountTest", result.getAccount());
        verify(repository).save(trade);
    }

    @Test
    void update_shouldUpdateTrade() {
        when(repository.findById(1)).thenReturn(Optional.of(trade));
        when(repository.save(any(Trade.class))).thenReturn(trade);

        Trade updated = new Trade();
        updated.setAccount("UpdatedAccount");
        updated.setType("UpdatedType");
        updated.setBuyQuantity(BigDecimal.valueOf(20.0));

        Trade result = service.update(1, updated);

        assertEquals("UpdatedAccount", result.getAccount());
        assertEquals("UpdatedType", result.getType());
        assertEquals(BigDecimal.valueOf(20.0), result.getBuyQuantity());

        verify(repository).save(any(Trade.class));
    }

    @Test
    void delete_shouldRemoveTrade() {
        when(repository.findById(1)).thenReturn(Optional.of(trade));

        service.delete(1);

        verify(repository).delete(trade);
    }
}
