package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurvePointServiceImpl implements CurvePointService {

    private final CurvePointRepository repository;

    public CurvePointServiceImpl(CurvePointRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CurvePoint> findAll() {
        return repository.findAll();
    }

    @Override
    public CurvePoint findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CurvePoint not found: " + id));
    }

    @Override
    public CurvePoint save(CurvePoint curvePoint) {
        return repository.save(curvePoint);
    }

    @Override
    public CurvePoint update(Integer id, CurvePoint updated) {
        CurvePoint existing = findById(id);

        existing.setCurveId(updated.getCurveId());
        existing.setTerm(updated.getTerm());
        existing.setValue(updated.getValue());

        return repository.save(existing);
    }


    @Override
    public void delete(Integer id) {
        repository.delete(findById(id));
    }
}

