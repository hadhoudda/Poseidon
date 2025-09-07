package com.nnk.springboot.services;

import com.nnk.springboot.model.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.contracts.IRatingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing Rating entities.
 */
@Service
public class RatingService implements IRatingService {

    private final RatingRepository ratingRepository;

    /**
     * Constructor for RatingService.
     *
     * @param ratingRepository the repository used for Rating entity operations
     */
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Save a Rating entity.
     *
     * @param rating the Rating entity to save
     * @return the saved Rating entity
     */
    @Override
    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    /**
     * Retrieve all Rating entities.
     *
     * @return a list of all Rating entities
     */
    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    /**
     * Find a Rating entity by its id.
     *
     * @param id the id of the Rating entity
     * @return an Optional containing the found Rating or empty if not found
     */
    @Override
    public Optional<Rating> findRatingById(Integer id) {
        return ratingRepository.findById(id);
    }

    /**
     * Update a Rating entity by id.
     *
     * @param id     the id of the Rating to update
     * @param rating the Rating entity containing updated data
     */
    @Override
    public void updateRating(Integer id, Rating rating) {
        rating.setId(id);
        ratingRepository.save(rating);
    }

    /**
     * Delete a Rating entity by id.
     *
     * @param id the id of the Rating to delete
     */
    @Override
    public void deleteRatingById(Integer id) {
        ratingRepository.deleteById(id);
    }
}
