package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
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

@WebMvcTest(BidListController.class)
class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    // -----------------------------
    // GET /bidList/list
    // -----------------------------
    @Test
    void testGetBidList() throws Exception {
        when(bidListService.findAll()).thenReturn(List.of(new BidList()));

        mockMvc.perform(get("/bidList/list")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"));
    }

    // -----------------------------
    // GET /bidList/add
    // -----------------------------
    @Test
    void testAddBidForm() throws Exception {
        mockMvc.perform(get("/bidList/add")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    // -----------------------------
    // POST /bidList/validate
    // -----------------------------
    @Test
    void testValidateBid() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .param("account", "TestAccount")
                        .param("type", "TypeTest")
                        .param("bidQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, times(1)).save(any(BidList.class));
    }

    // -----------------------------
    // GET /bidList/update/{id}
    // -----------------------------
    @Test
    void testShowUpdateForm() throws Exception {
        BidList bid = new BidList();
        bid.setBidListId(1);
        bid.setAccount("Test");
        bid.setType("Type");
        bid.setBidQuantity(BigDecimal.valueOf(10.0));

        when(bidListService.findById(1)).thenReturn(bid);

        mockMvc.perform(get("/bidList/update/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"));
    }

    // -----------------------------
    // POST /bidList/update/{id}
    // -----------------------------
    @Test
    void testUpdateBid() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("bidQuantity", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, times(1)).update(eq(1), any(BidList.class));
    }

    // -----------------------------
    // GET /bidList/delete/{id}
    // -----------------------------
    @Test
    void testDeleteBid() throws Exception {
        mockMvc.perform(get("/bidList/delete/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, times(1)).delete(1);
    }
}
