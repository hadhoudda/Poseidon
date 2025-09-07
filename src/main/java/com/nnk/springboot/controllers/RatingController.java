package com.nnk.springboot.controllers;

import com.nnk.springboot.model.Rating;
import com.nnk.springboot.services.contracts.IRatingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing {@link Rating} entities.
 * Handles CRUD operations for credit ratings (Moody's, S&P, Fitch).
 */
@Controller
public class RatingController {

    private static final Logger logger = LogManager.getLogger(RatingController.class);
    private static final String REDIRECT_RATING_LIST = "redirect:/rating/list";

    @Autowired
    private IRatingService iRatingService;

    /**
     * Displays the list of all ratings.
     *
     * @param model Spring MVC model
     * @return view name for the rating list
     */
    @RequestMapping("/rating/list")
    public String listRatings(Model model) {
        model.addAttribute("ratings", iRatingService.getAllRatings());
        logger.info("Displaying rating list");
        return "rating/list";
    }

    /**
     * Displays the form to add a new rating.
     *
     * @param rating empty Rating object for form binding
     * @return view name for add form
     */
    @GetMapping("/rating/add")
    public String showAddRatingForm(Rating rating) {
        logger.info("Displaying add rating form");
        return "rating/add";
    }

    /**
     * Validates and saves a new Rating entity.
     *
     * @param rating Rating object from the form
     * @param result validation result
     * @param model  Spring MVC model
     * @return redirect to list or return to form if validation fails
     */
    @PostMapping("/rating/validate")
    public String validateRating(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Rating validation failed: {}", result.getAllErrors());
            return "rating/add";
        }

        iRatingService.saveRating(rating);
        logger.info("Rating created: Moody's={}, S&P={}, Fitch={}, Order={}",
                rating.getMoodysRating(), rating.getSandPRating(),
                rating.getFitchRating(), rating.getOrderNumber());

        return REDIRECT_RATING_LIST;
    }

    /**
     * Displays the form to update an existing rating.
     *
     * @param id    ID of the rating to update
     * @param model Spring MVC model
     * @return view name for update form
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Rating rating = iRatingService.findRatingById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid rating ID for update: {}", id);
                    return new IllegalArgumentException("Invalid rating ID: " + id);
                });

        model.addAttribute("rating", rating);
        logger.info("Displaying update form for rating ID: {}", id);
        return "rating/update";
    }

    /**
     * Validates and updates an existing rating.
     *
     * @param id     ID of the rating to update
     * @param rating updated rating data from the form
     * @param result validation result
     * @param model  Spring MVC model
     * @return redirect to list or return to form if validation fails
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Rating update failed for ID {}: {}", id, result.getAllErrors());
            return "rating/update";
        }

        // Retrieve and update the existing rating
        Rating existingRating = iRatingService.findRatingById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid rating ID during update: {}", id);
                    return new IllegalArgumentException("Invalid rating ID: " + id);
                });

        existingRating.setMoodysRating(rating.getMoodysRating());
        existingRating.setSandPRating(rating.getSandPRating());
        existingRating.setFitchRating(rating.getFitchRating());
        existingRating.setOrderNumber(rating.getOrderNumber());

        iRatingService.saveRating(existingRating);
        logger.info("Rating updated: ID={}, Moody's={}, S&P={}, Fitch={}, Order={}",
                id, rating.getMoodysRating(), rating.getSandPRating(),
                rating.getFitchRating(), rating.getOrderNumber());

        return REDIRECT_RATING_LIST;
    }

    /**
     * Deletes a rating by ID.
     *
     * @param id    ID of the rating to delete
     * @param model Spring MVC model
     * @return redirect to rating list
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        Rating existingRating = iRatingService.findRatingById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid rating ID for deletion: {}", id);
                    return new IllegalArgumentException("Invalid rating ID: " + id);
                });

        iRatingService.deleteRatingById(existingRating.getId());
        logger.info("Rating deleted: ID={}, Moody's={}, S&P={}, Fitch={}",
                id, existingRating.getMoodysRating(), existingRating.getSandPRating(),
                existingRating.getFitchRating());

        return REDIRECT_RATING_LIST;
    }

    /**
     * Adds the current logged-in user's name to the model under "remoteUser".
     *
     * @param httpServletRequest the HTTP request
     * @return remote user name
     */
    @ModelAttribute("remoteUser")
    public Object remoteUser(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteUser();
    }
}
