package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListServiceImpl implements BidListService {

    private final BidListRepository repository;

    public BidListServiceImpl(BidListRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BidList> findAll() {
        return repository.findAll();
    }

    @Override
    public BidList findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BidList not found: " + id));
    }

    @Override
    public BidList save(BidList bidList) {
        return repository.save(bidList);
    }

    @Override
    public BidList update(Integer id, BidList updated) {
        BidList existing = findById(id);

        existing.setAccount(updated.getAccount());
        existing.setType(updated.getType());
        existing.setBidQuantity(updated.getBidQuantity());

        return repository.save(existing);
    }


    @Override
    public void delete(Integer id) {
        repository.delete(findById(id));
    }
}
