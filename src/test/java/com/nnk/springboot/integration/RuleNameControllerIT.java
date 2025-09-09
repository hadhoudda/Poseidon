package com.nnk.springboot.integration;

import com.nnk.springboot.model.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RuleNameControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @BeforeEach
    void setup() {
        ruleNameRepository.deleteAll();

        RuleName r1 = new RuleName("Rule A", "Desc A", "JSON A", "Template A", "SQL A", "SQL Part A");
        RuleName r2 = new RuleName("Rule B", "Desc B", "JSON B", "Template B", "SQL B", "SQL Part B");

        ruleNameRepository.save(r1);
        ruleNameRepository.save(r2);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testListRuleNames_shouldReturnListView() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowAddRuleForm_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testValidateRuleName_shouldSaveAndRedirect() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("name", "Test Rule")
                        .param("description", "Test Description")
                        .param("json", "Test JSON")
                        .param("template", "Test Template")
                        .param("sqlStr", "SELECT *")
                        .param("sqlPart", "WHERE id = 1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        assertEquals(3, ruleNameRepository.count());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowUpdateForm_shouldReturnUpdateView() throws Exception {
        RuleName rule = ruleNameRepository.findAll().get(0);

        mockMvc.perform(get("/ruleName/update/" + rule.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUpdateRuleName_shouldUpdateAndRedirect() throws Exception {
        RuleName rule = ruleNameRepository.findAll().get(0);

        mockMvc.perform(post("/ruleName/update/" + rule.getId())
                        .with(csrf())
                        .param("name", "Updated Rule")
                        .param("description", "Updated Description")
                        .param("json", "Updated JSON")
                        .param("template", "Updated Template")
                        .param("sqlStr", "UPDATE test")
                        .param("sqlPart", "SET val = 1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        RuleName updated = ruleNameRepository.findById(rule.getId()).orElseThrow();
        assertEquals("Updated Rule", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals("Updated JSON", updated.getJson());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testDeleteRuleName_shouldRemoveAndRedirect() throws Exception {
        RuleName rule = ruleNameRepository.findAll().get(0);

        mockMvc.perform(get("/ruleName/delete/" + rule.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        assertFalse(ruleNameRepository.findById(rule.getId()).isPresent());
    }
}
