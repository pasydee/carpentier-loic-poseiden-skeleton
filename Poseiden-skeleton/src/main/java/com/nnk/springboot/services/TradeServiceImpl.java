package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {

    private final TradeRepository repository;

    public TradeServiceImpl(TradeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Trade> findAll() {
        return repository.findAll();
    }

    @Override
    public Trade findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found: " + id));
    }

    @Override
    public Trade save(Trade trade) {
        return repository.save(trade);
    }

    @Override
    public Trade update(Integer id, Trade updated) {
        Trade existing = findById(id);

        existing.setAccount(updated.getAccount());
        existing.setType(updated.getType());
        existing.setBuyQuantity(updated.getBuyQuantity());

        return repository.save(existing);
    }


    @Override
    public void delete(Integer id) {
        repository.delete(findById(id));
    }
}

