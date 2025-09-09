package com.nnk.springboot.integration;

import com.nnk.springboot.model.User;
import com.nnk.springboot.repositories.UserRepository;
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
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user1 = new User( "user1", "pass1", "User One", "USER");
        User user2 = new User( "user2", "pass2", "User Two", "ADMIN");

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testListUsers() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testShowAddUserForm() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testValidateUser_Success() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "newUser")
                        .param("password", "newPassword")
                        .param("fullname", "New User")
                        .param("role", "USER")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        // Vérifier que l'utilisateur est bien ajouté
        assert userRepository.findByUsername("newUser").isPresent();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testValidateUser_FailValidation() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("password", "password")
                        .param("fullname", "fullname")
                        .param("role", "USER")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testShowUpdateForm() throws Exception {
        Integer userId = userRepository.findAll().get(0).getId();

        mockMvc.perform(get("/user/update/" + userId))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser_Success() throws Exception {
        Integer userId = userRepository.findAll().get(0).getId();

        mockMvc.perform(post("/user/update/" + userId)
                        .with(csrf())
                        .param("username", "updatedUser")
                        .param("password", "updatedPass")
                        .param("fullname", "Updated Name")
                        .param("role", "USER")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        // Vérifier la mise à jour
        User updated = userRepository.findById(userId).orElseThrow();
        assert updated.getUsername().equals("updatedUser");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser_FailValidation() throws Exception {
        Integer userId = userRepository.findAll().get(0).getId();

        mockMvc.perform(post("/user/update/" + userId)
                        .with(csrf())
                        .param("password", "short")
                        .param("fullname", "No Username")
                        .param("role", "USER")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        Integer userId = userRepository.findAll().get(0).getId();

        mockMvc.perform(get("/user/delete/" + userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        assert userRepository.findById(userId).isEmpty();
    }
}
