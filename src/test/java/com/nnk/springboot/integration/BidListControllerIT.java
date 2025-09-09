package com.nnk.springboot.integration;

import com.nnk.springboot.model.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BidListControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidListRepository bidListRepository;

    @BeforeEach
    void setup() {
        bidListRepository.deleteAll();

        BidList bid1 = new BidList("Account Test 1", "Type Test 1", 10.0);
        BidList bid2 = new BidList("Account Test 2", "Type Test 2", 20.0);
        bidListRepository.save(bid1);
        bidListRepository.save(bid2);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testHome_ShouldReturnBidListView() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attribute("bidLists", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddBidForm_ShouldDisplayAddForm() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testValidateBid_Success() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", "New Account")
                        .param("type", "New Type")
                        .param("bidQuantity", "99.9")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testValidateBid_FailValidation() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("type", "Missing Account") // account is missing
                        .param("bidQuantity", "12.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testShowUpdateForm_ShouldDisplayForm() throws Exception {
        Integer id = bidListRepository.findAll().get(0).getBidListId();

        mockMvc.perform(get("/bidList/update/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateBid_Success() throws Exception {
        Integer id = bidListRepository.findAll().get(0).getBidListId();

        mockMvc.perform(post("/bidList/update/" + id)
                        .with(csrf())
                        .param("account", "Updated Account")
                        .param("type", "Updated Type")
                        .param("bidQuantity", "77.7")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateBid_FailValidation() throws Exception {
        Integer id = bidListRepository.findAll().get(0).getBidListId();

        mockMvc.perform(post("/bidList/update/" + id)
                        .with(csrf())
                        .param("account", "") // empty account = error
                        .param("type", "Type")
                        .param("bidQuantity", "123.4")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteBid_ShouldRemoveBid() throws Exception {
        Integer id = bidListRepository.findAll().get(0).getBidListId();

        mockMvc.perform(get("/bidList/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }
}
