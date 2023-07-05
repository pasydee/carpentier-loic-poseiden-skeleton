package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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

class BidListServiceImplTest {

    @Mock
    private BidListRepository repository;

    @InjectMocks
    private BidListServiceImpl service;

    private BidList bid;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bid = new BidList();
        bid.setBidListId(1);
        bid.setAccount("AccountTest");
        bid.setType("TypeTest");
        bid.setBidQuantity(BigDecimal.valueOf(10));
    }

    @Test
    void findAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(bid));

        List<BidList> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void findById_shouldReturnBid() {
        when(repository.findById(1)).thenReturn(Optional.of(bid));

        BidList result = service.findById(1);

        assertEquals("AccountTest", result.getAccount());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(1));
    }

    @Test
    void save_shouldSaveBid() {
        when(repository.save(bid)).thenReturn(bid);

        BidList result = service.save(bid);

        assertEquals("AccountTest", result.getAccount());
        verify(repository).save(bid);
    }

    @Test
    void update_shouldUpdateBid() {
        when(repository.findById(1)).thenReturn(Optional.of(bid));
        when(repository.save(any(BidList.class))).thenReturn(bid);

        BidList updated = new BidList();
        updated.setAccount("Updated");
        updated.setType("UpdatedType");
        updated.setBidQuantity(BigDecimal.valueOf(20));

        BidList result = service.update(1, updated);

        assertEquals("Updated", result.getAccount());
        verify(repository).save(any(BidList.class));
    }

    @Test
    void delete_shouldRemoveBid() {
        when(repository.findById(1)).thenReturn(Optional.of(bid));

        service.delete(1);

        verify(repository).delete(bid);
    }
}
