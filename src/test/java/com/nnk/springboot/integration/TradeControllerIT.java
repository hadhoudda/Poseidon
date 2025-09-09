package com.nnk.springboot.integration;

import com.nnk.springboot.model.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeRepository tradeRepository;

    @BeforeEach
    void setup() {
        tradeRepository.deleteAll();

        Trade t1 = new Trade();
        t1.setAccount("Account A");
        t1.setType("Type A");
        t1.setBuyQuantity(10d);
        tradeRepository.save(t1);

        Trade t2 = new Trade();
        t2.setAccount("Account B");
        t2.setType("Type B");
        t2.setBuyQuantity(20d);
        tradeRepository.save(t2);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testListTrades_shouldReturnTradeListViewWithModel() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowAddTradeForm_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testValidateTrade_shouldSaveTradeAndRedirect() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .param("buyQuantity", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowUpdateForm_shouldReturnUpdateView() throws Exception {
        Trade trade = new Trade("Account1", "Type1", 50.0);
        trade = tradeRepository.save(trade);

        mockMvc.perform(get("/trade/update/" + trade.getTradeId()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUpdateTrade_shouldUpdateAndRedirect() throws Exception {
        Trade trade = new Trade("OldAccount", "OldType", 10.0);
        trade = tradeRepository.save(trade);

        mockMvc.perform(post("/trade/update/" + trade.getTradeId())
                        .with(csrf())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("buyQuantity", "200"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        Trade updated = tradeRepository.findById(trade.getTradeId()).orElseThrow();
        assertEquals("UpdatedAccount", updated.getAccount());
        assertEquals("UpdatedType", updated.getType());
        assertEquals(200.0, updated.getBuyQuantity());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testDeleteTrade_shouldRemoveTradeAndRedirect() throws Exception {
        Trade trade = new Trade("AccountToDelete", "Type", 50.0);
        trade = tradeRepository.save(trade);

        mockMvc.perform(get("/trade/delete/" + trade.getTradeId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        assertFalse(tradeRepository.findById(trade.getTradeId()).isPresent());
    }


}
