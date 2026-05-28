package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurveController.class)
class CurvePointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointService curvePointService;

    // -----------------------------
    // GET /curvePoint/list
    // -----------------------------
    @Test
    void testGetCurvePointList() throws Exception {
        when(curvePointService.findAll()).thenReturn(List.of(new CurvePoint()));

        mockMvc.perform(get("/curvePoint/list")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"));
    }

    // -----------------------------
    // GET /curvePoint/add
    // -----------------------------
    @Test
    void testAddCurvePointForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    // -----------------------------
    // POST /curvePoint/validate
    // -----------------------------
    @Test
    void testValidateCurvePoint() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .param("curveId", "10")
                        .param("term", "20")
                        .param("value", "30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).save(any(CurvePoint.class));
    }

    // -----------------------------
    // GET /curvePoint/update/{id}
    // -----------------------------
    @Test
    void testShowUpdateForm() throws Exception {
        CurvePoint curve = new CurvePoint();
        curve.setId(1);
        curve.setCurveId(10);
        curve.setTerm(BigDecimal.valueOf(20));
        curve.setValue(BigDecimal.valueOf(30));

        when(curvePointService.findById(1)).thenReturn(curve);

        mockMvc.perform(get("/curvePoint/update/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));
    }

    // -----------------------------
    // POST /curvePoint/update/{id}
    // -----------------------------
    @Test
    void testUpdateCurvePoint() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .param("curveId", "99")
                        .param("term", "50")
                        .param("value", "60"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).update(eq(1), any(CurvePoint.class));
    }

    // -----------------------------
    // GET /curvePoint/delete/{id}
    // -----------------------------
    @Test
    void testDeleteCurvePoint() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).delete(1);
    }
}
