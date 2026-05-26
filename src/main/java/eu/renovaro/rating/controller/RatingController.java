package eu.renovaro.rating.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.renovaro.rating.domain.CreateReviewRequest;
import eu.renovaro.rating.domain.ReviewDto;
import eu.renovaro.rating.service.RatingService;
import eu.renovaro.user.service.UserProfileService;
import eu.renovaro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rating")
public class RatingController {

    private final UserProfileService userProfileService;
    private final RatingService ratingService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping("/provider/reviews")
    public List<ReviewDto> getReviewsForProvider() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Long userProfileId = userProfileService.getUserProfileIdByEmail(email);
        return ratingService.getPrivateReviewsByUserProfileId(userProfileId);
    }

    @PostMapping(value = "/create-review", consumes = "multipart/form-data")
    public ResponseEntity<Void> createReview(
            @RequestPart("payload") String payload,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws JsonProcessingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByEmail(auth.getName());

        CreateReviewRequest request = objectMapper.readValue(payload, CreateReviewRequest.class);
        ratingService.createReview(userId, request, file);

        return ResponseEntity.ok().build();
    }
//    //TODO TEST POSTMAN
//@PostMapping("/create-review")
//public ResponseEntity<Void> createReview(@RequestBody CreateReviewRequest request) {
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    Long userId = userService.getUserInfoByEmail(auth.getName()).getUserId();
//
//    ratingService.createReview(userId, request, null);
//    return ResponseEntity.ok().build();
//}
}
