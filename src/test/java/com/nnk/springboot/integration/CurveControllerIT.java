package com.nnk.springboot.integration;

import com.nnk.springboot.model.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
class CurveControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @BeforeEach
    void setup() {
        curvePointRepository.deleteAll();

        CurvePoint cp1 = new CurvePoint();
        cp1.setCurveId(1);
        cp1.setTerm(10.0);
        cp1.setValue(100.0);
        curvePointRepository.save(cp1);

        CurvePoint cp2 = new CurvePoint();
        cp2.setCurveId(2);
        cp2.setTerm(20.0);
        cp2.setValue(200.0);
        curvePointRepository.save(cp2);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testListCurvePoints_shouldReturnListView() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddCurvePointForm_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testValidateCurvePoint_shouldSaveAndRedirect() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("curveId", "3")
                        .param("term", "30.0")
                        .param("value", "300.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        assertEquals(3, curvePointRepository.count());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowUpdateForm_shouldReturnUpdateView() throws Exception {
        CurvePoint cp = new CurvePoint();
        cp.setCurveId(4);
        cp.setTerm(40.0);
        cp.setValue(400.0);
        CurvePoint saved = curvePointRepository.save(cp);

        mockMvc.perform(get("/curvePoint/update/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUpdateCurvePoint_shouldUpdateAndRedirect() throws Exception {
        CurvePoint cp = new CurvePoint();
        cp.setCurveId(5);
        cp.setTerm(50.0);
        cp.setValue(500.0);
        CurvePoint saved = curvePointRepository.save(cp);

        mockMvc.perform(post("/curvePoint/update/" + saved.getId())
                        .with(csrf())
                        .param("curveId", String.valueOf(saved.getCurveId()))
                        .param("term", "55.5")
                        .param("value", "555.5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        CurvePoint updated = curvePointRepository.findById(saved.getId()).orElseThrow();
        assertEquals(55.5, updated.getTerm());
        assertEquals(555.5, updated.getValue());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testDeleteCurvePoint_shouldDeleteAndRedirect() throws Exception {
        CurvePoint cp = new CurvePoint();
        cp.setCurveId(6);
        cp.setTerm(60.0);
        cp.setValue(600.0);
        CurvePoint saved = curvePointRepository.save(cp);

        mockMvc.perform(get("/curvePoint/delete/" + saved.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        assertFalse(curvePointRepository.findById(saved.getId()).isPresent());
    }
}
