package com.nnk.springboot.controllers;

import com.nnk.springboot.model.User;
import com.nnk.springboot.services.contracts.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing {@link User} entities.
 * Handles operations like creating, updating, listing, and deleting users.
 */
@Controller
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private static final String REDIRECT_USER_LIST = "redirect:/user/list";

    @Autowired
    private IUserService iUserService;

    /**
     * Displays the list of all users.
     *
     * @param model Spring MVC model
     * @return view name for user list
     */
    @RequestMapping("/user/list")
    public String listUsers(Model model) {
        model.addAttribute("users", iUserService.getAllUsers());
        logger.info("Displaying user list");
        return "user/list";
    }

    /**
     * Shows the form to add a new user.
     *
     * @param user empty User object to bind the form
     * @return view name for add user form
     */
    @GetMapping("/user/add")
    public String showAddUserForm(User user) {
        logger.info("Displaying add user form");
        return "user/add";
    }

    /**
     * Validates and saves a new user to the database.
     * Password is hashed using BCrypt before saving.
     *
     * @param user   the user to validate and save
     * @param result binding result for validation
     * @param model  Spring MVC model
     * @return redirection to user list or back to form if error
     */
    @PostMapping("/user/validate")
    public String validateUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("User validation failed: {}", result.getAllErrors());
            return "user/add";
        }

        // Hash password before saving
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        iUserService.saveUser(user);
        logger.info("User created: {}", user.getUsername());

        return REDIRECT_USER_LIST;
    }

    /**
     * Displays the update form for an existing user.
     *
     * @param id    the ID of the user to update
     * @param model Spring MVC model
     * @return view name for update user form
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = iUserService.findById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid user ID for update: {}", id);
                    return new IllegalArgumentException("Invalid user ID: " + id);
                });

        // Clear password field to avoid pre-filling in the form
        user.setPassword("");

        model.addAttribute("user", user);
        logger.info("Displaying update form for user ID: {}", id);
        return "user/update";
    }

    /**
     * Updates an existing user in the database.
     * Password is re-hashed even if unchanged (can be improved).
     *
     * @param id     ID of the user being updated
     * @param user   the updated user data
     * @param result binding result for validation
     * @param model  Spring MVC model
     * @return redirection to user list or back to form if error
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("User update failed for ID {}: {}", id, result.getAllErrors());
            return "user/update";
        }

        // Re-encode password (even if it didn't change)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);

        iUserService.saveUser(user);
        logger.info("User updated: ID={}, username={}", id, user.getUsername());

        return REDIRECT_USER_LIST;
    }

    /**
     * Deletes a user by ID.
     *
     * @param id    the ID of the user to delete
     * @param model Spring MVC model
     * @return redirection to user list
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = iUserService.findById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid user ID for deletion: {}", id);
                    return new IllegalArgumentException("Invalid user ID: " + id);
                });

        iUserService.deleteById(id);
        logger.info("User deleted: ID={}, username={}", id, user.getUsername());

        return REDIRECT_USER_LIST;
    }

    /**
     * Adds the current remote (logged-in) username to all models.
     *
     * @param httpServletRequest the HTTP request
     * @return the remote user's username
     */
    @ModelAttribute("remoteUser")
    public Object remoteUser(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteUser();
    }
}
