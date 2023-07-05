package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleNameServiceImpl implements RuleNameService {

    private final RuleNameRepository repository;

    public RuleNameServiceImpl(RuleNameRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RuleName> findAll() {
        return repository.findAll();
    }

    @Override
    public RuleName findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RuleName not found: " + id));
    }

    @Override
    public RuleName save(RuleName ruleName) {
        return repository.save(ruleName);
    }

    @Override
    public RuleName update(Integer id, RuleName updated) {
        RuleName existing = findById(id);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setJson(updated.getJson());
        existing.setTemplate(updated.getTemplate());
        existing.setSqlStr(updated.getSqlStr());
        existing.setSqlPart(updated.getSqlPart());

        return repository.save(existing);
    }


    @Override
    public void delete(Integer id) {
        repository.delete(findById(id));
    }
}

