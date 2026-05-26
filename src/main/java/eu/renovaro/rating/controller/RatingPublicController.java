package eu.renovaro.rating.controller;

import eu.renovaro.rating.domain.ReviewDto;
import eu.renovaro.rating.domain.UserRatingDto;
import eu.renovaro.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/rating")
public class RatingPublicController {

    private final RatingService ratingService;

    @GetMapping("/reviews/{userProfileId}")
    public List<ReviewDto> getReviewsByUserProfileId(@PathVariable Long userProfileId) {
        return ratingService.getPublicReviewsByUserProfileId(userProfileId);
    }

    @GetMapping("/rating-breakdown/{userProfileId}")
    public UserRatingDto getRatingBreakdown(@PathVariable Long userProfileId) {
        return ratingService.getRatingBreakdownByUserProfileId(userProfileId);
    }
}
