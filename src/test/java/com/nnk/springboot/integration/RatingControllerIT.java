package com.nnk.springboot.integration;

import com.nnk.springboot.model.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class RatingControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    @BeforeEach
    void setup() {
        ratingRepository.deleteAll();

        // Ajoute des ratings de test
        Rating r1 = new Rating();
        r1.setMoodysRating("Aaa");
        r1.setSandPRating("AA+");
        r1.setFitchRating("AAA");
        r1.setOrderNumber(1);
        ratingRepository.save(r1);

        Rating r2 = new Rating();
        r2.setMoodysRating("Bbb");
        r2.setSandPRating("BB");
        r2.setFitchRating("BBB");
        r2.setOrderNumber(2);
        ratingRepository.save(r2);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testListRatings() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", hasSize(2)));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testValidateRating() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating", "Ccc")
                        .param("sandPRating", "CC")
                        .param("fitchRating", "CCC")
                        .param("orderNumber", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        assert(ratingRepository.count() == 3);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowUpdateForm() throws Exception {
        Integer id = ratingRepository.findAll().get(0).getId();

        mockMvc.perform(get("/rating/update/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("rating"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testUpdateRating() throws Exception {
        Rating rating = ratingRepository.findAll().get(0);

        mockMvc.perform(post("/rating/update/" + rating.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("moodysRating", "Updated")
                        .param("sandPRating", "Updated")
                        .param("fitchRating", "Updated")
                        .param("orderNumber", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        Rating updated = ratingRepository.findById(rating.getId()).orElseThrow();
        assert(updated.getMoodysRating().equals("Updated"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testDeleteRating() throws Exception {
        Rating rating = ratingRepository.findAll().get(0);
        long countBefore = ratingRepository.count();

        mockMvc.perform(get("/rating/delete/" + rating.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        long countAfter = ratingRepository.count();
        assert(countAfter == countBefore - 1);
    }
}
