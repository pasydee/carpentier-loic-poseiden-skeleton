package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RuleNameController.class)
class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    // -----------------------------
    // GET /ruleName/list
    // -----------------------------
    @Test
    void testGetRuleNameList() throws Exception {
        when(ruleNameService.findAll()).thenReturn(List.of(new RuleName()));

        mockMvc.perform(get("/ruleName/list")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"));
    }

    // -----------------------------
    // GET /ruleName/add
    // -----------------------------
    @Test
    void testAddRuleNameForm() throws Exception {
        mockMvc.perform(get("/ruleName/add")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    // -----------------------------
    // POST /ruleName/validate
    // -----------------------------
    @Test
    void testValidateRuleName() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .param("name", "Rule Test")
                        .param("description", "Description Test")
                        .param("json", "json")
                        .param("template", "template")
                        .param("sqlStr", "sql")
                        .param("sqlPart", "sqlPart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).save(any(RuleName.class));
    }

    // -----------------------------
    // GET /ruleName/update/{id}
    // -----------------------------
    @Test
    void testShowUpdateForm() throws Exception {
        RuleName rule = new RuleName();
        rule.setId(1);
        rule.setName("Rule");
        rule.setDescription("Desc");
        rule.setJson("json");
        rule.setTemplate("template");
        rule.setSqlStr("sql");
        rule.setSqlPart("sqlPart");

        when(ruleNameService.findById(1)).thenReturn(rule);

        mockMvc.perform(get("/ruleName/update/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));
    }

    // -----------------------------
    // POST /ruleName/update/{id}
    // -----------------------------
    @Test
    void testUpdateRuleName() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .param("name", "Updated Rule")
                        .param("description", "Updated Desc")
                        .param("json", "json2")
                        .param("template", "template2")
                        .param("sqlStr", "sql2")
                        .param("sqlPart", "sqlPart2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).update(eq(1), any(RuleName.class));
    }

    // -----------------------------
    // GET /ruleName/delete/{id}
    // -----------------------------
    @Test
    void testDeleteRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).delete(1);
    }
}
