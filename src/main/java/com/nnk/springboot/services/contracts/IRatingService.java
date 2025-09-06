package com.nnk.springboot.services.contracts;

import com.nnk.springboot.model.Rating;

import java.util.List;
import java.util.Optional;

public interface IRatingService {
    Rating saveRating(Rating rating);

    List<Rating> getAllRatings();

    Optional<Rating> findRatingById(Integer id);

    void updateRating(Integer id, Rating rating);

    void deleteRatingById(Integer id);
}
