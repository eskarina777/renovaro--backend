package eu.renovaro.rating.service;

import eu.renovaro.rating.domain.CreateReviewRequest;
import eu.renovaro.rating.domain.ReviewDto;
import eu.renovaro.rating.domain.UserRatingDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RatingService {
    List<ReviewDto> getPublicReviewsByUserProfileId(Long userProfileId);
    List<ReviewDto> getPrivateReviewsByUserProfileId(Long userProfileId);
    UserRatingDto getRatingBreakdownByUserProfileId(Long userProfileId);

    void createReview(Long userId, CreateReviewRequest request, MultipartFile file);

}
