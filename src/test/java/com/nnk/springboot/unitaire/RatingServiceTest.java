package com.nnk.springboot.unitaire;

import com.nnk.springboot.model.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("Aaa");
        rating.setSandPRating("AAA");
        rating.setFitchRating("AAA");
        rating.setOrderNumber(10);
    }

    @Test
    void saveRating_ShouldReturnSavedRating() {
        when(ratingRepository.save(rating)).thenReturn(rating);

        Rating savedRating = ratingService.saveRating(rating);

        verify(ratingRepository, times(1)).save(rating);
        assertEquals(rating, savedRating);
    }

    @Test
    void getAllRatings_ShouldReturnListOfRatings() {
        List<Rating> ratings = Arrays.asList(rating, new Rating());

        when(ratingRepository.findAll()).thenReturn(ratings);

        List<Rating> result = ratingService.getAllRatings();

        verify(ratingRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    void findRatingById_ShouldReturnRating() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        Optional<Rating> foundRating = ratingService.findRatingById(1);

        verify(ratingRepository, times(1)).findById(1);
        assertTrue(foundRating.isPresent());
        assertEquals(1, foundRating.get().getId());
    }

    @Test
    void updateRating_ShouldSetIdAndSave() {
        Rating updatedRating = new Rating();
        updatedRating.setMoodysRating("Baa");

        when(ratingRepository.save(any(Rating.class))).thenReturn(updatedRating);

        ratingService.updateRating(1, updatedRating);

        ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepository).save(captor.capture());
        Rating savedRating = captor.getValue();

        assertEquals(1, savedRating.getId());
        assertEquals("Baa", savedRating.getMoodysRating());
    }

    @Test
    void deleteRatingById_ShouldCallDeleteById() {
        doNothing().when(ratingRepository).deleteById(1);

        ratingService.deleteRatingById(1);

        verify(ratingRepository, times(1)).deleteById(1);
    }
}
