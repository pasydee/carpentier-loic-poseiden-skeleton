package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository repository;

    public RatingServiceImpl(RatingRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Rating> findAll() {
        return repository.findAll();
    }

    @Override
    public Rating findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found: " + id));
    }

    @Override
    public Rating save(Rating rating) {
        return repository.save(rating);
    }

    @Override
    public Rating update(Integer id, Rating updated) {
        Rating existing = findById(id);

        existing.setMoodysRating(updated.getMoodysRating());
        existing.setSandPRating(updated.getSandPRating());
        existing.setFitchRating(updated.getFitchRating());
        existing.setOrderNumber(updated.getOrderNumber());

        return repository.save(existing);
    }


    @Override
    public void delete(Integer id) {
        repository.delete(findById(id));
    }
}

