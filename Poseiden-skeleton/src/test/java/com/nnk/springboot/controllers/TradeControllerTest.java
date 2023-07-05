package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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

@WebMvcTest(TradeController.class)
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    // -----------------------------
    // GET /trade/list
    // -----------------------------
    @Test
    void testGetTradeList() throws Exception {
        when(tradeService.findAll()).thenReturn(List.of(new Trade()));

        mockMvc.perform(get("/trade/list")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"));
    }

    // -----------------------------
    // GET /trade/add
    // -----------------------------
    @Test
    void testAddTradeForm() throws Exception {
        mockMvc.perform(get("/trade/add")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    // -----------------------------
    // POST /trade/validate
    // -----------------------------
    @Test
    void testValidateTrade() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .param("account", "TestAccount")
                        .param("type", "TypeTest")
                        .param("buyQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).save(any(Trade.class));
    }

    // -----------------------------
    // GET /trade/update/{id}
    // -----------------------------
    @Test
    void testShowUpdateForm() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("TestAccount");
        trade.setType("TypeTest");
        trade.setBuyQuantity(BigDecimal.valueOf(10.0));

        when(tradeService.findById(1)).thenReturn(trade);

        mockMvc.perform(get("/trade/update/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));
    }

    // -----------------------------
    // POST /trade/update/{id}
    // -----------------------------
    @Test
    void testUpdateTrade() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("buyQuantity", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).update(eq(1), any(Trade.class));
    }

    // -----------------------------
    // GET /trade/delete/{id}
    // -----------------------------
    @Test
    void testDeleteTrade() throws Exception {
        mockMvc.perform(get("/trade/delete/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).delete(1);
    }
}
