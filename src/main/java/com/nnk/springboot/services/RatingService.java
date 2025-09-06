package com.nnk.springboot.services;

import com.nnk.springboot.model.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.contracts.IRatingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService implements IRatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public Optional<Rating> findRatingById(Integer id) {
        return ratingRepository.findById(id);
    }

    @Override
    public void updateRating(Integer id, Rating rating) {
        rating.setId(id);
        ratingRepository.save(rating);
    }

    @Override
    public void deleteRatingById(Integer id) {
        ratingRepository.deleteById(id);
    }
}
