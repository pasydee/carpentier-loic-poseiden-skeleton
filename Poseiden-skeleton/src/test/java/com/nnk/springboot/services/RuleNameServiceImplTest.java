package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RuleNameServiceImplTest {

    @Mock
    private RuleNameRepository repository;

    @InjectMocks
    private RuleNameServiceImpl service;

    private RuleName rule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rule = new RuleName();
        rule.setId(1);
        rule.setName("Rule");
        rule.setDescription("Description");
        rule.setJson("json");
        rule.setTemplate("template");
        rule.setSqlStr("sql");
        rule.setSqlPart("sqlPart");
    }

    @Test
    void findAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(rule));

        List<RuleName> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void findById_shouldReturnRuleName() {
        when(repository.findById(1)).thenReturn(Optional.of(rule));

        RuleName result = service.findById(1);

        assertEquals("Rule", result.getName());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(1));
    }

    @Test
    void save_shouldSaveRuleName() {
        when(repository.save(rule)).thenReturn(rule);

        RuleName result = service.save(rule);

        assertEquals("Rule", result.getName());
        verify(repository).save(rule);
    }

    @Test
    void update_shouldUpdateRuleName() {
        when(repository.findById(1)).thenReturn(Optional.of(rule));
        when(repository.save(any(RuleName.class))).thenReturn(rule);

        RuleName updated = new RuleName();
        updated.setName("Updated Rule");
        updated.setDescription("Updated Desc");
        updated.setJson("json2");
        updated.setTemplate("template2");
        updated.setSqlStr("sql2");
        updated.setSqlPart("sqlPart2");

        RuleName result = service.update(1, updated);

        assertEquals("Updated Rule", result.getName());
        assertEquals("Updated Desc", result.getDescription());
        assertEquals("json2", result.getJson());
        assertEquals("template2", result.getTemplate());
        assertEquals("sql2", result.getSqlStr());
        assertEquals("sqlPart2", result.getSqlPart());

        verify(repository).save(any(RuleName.class));
    }

    @Test
    void delete_shouldRemoveRuleName() {
        when(repository.findById(1)).thenReturn(Optional.of(rule));

        service.delete(1);

        verify(repository).delete(rule);
    }
}
