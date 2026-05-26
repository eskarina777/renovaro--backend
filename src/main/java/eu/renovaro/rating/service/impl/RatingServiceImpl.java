package eu.renovaro.rating.service.impl;

import eu.renovaro.ad.domain.ServiceTypeCode;
import eu.renovaro.ad.domain.entity.Ad;
import eu.renovaro.ad.domain.entity.FreelancePackage;
import eu.renovaro.ad.repository.FreelancePackageRepository;
import eu.renovaro.common.service.CloudinaryService;
import eu.renovaro.rating.domain.CreateReviewRequest;
import eu.renovaro.rating.domain.RatingCriteriaDto;
import eu.renovaro.rating.domain.ReviewDto;
import eu.renovaro.rating.domain.UserRatingDto;
import eu.renovaro.rating.domain.entity.Review;
import eu.renovaro.rating.domain.entity.ReviewRatingCriteria;
import eu.renovaro.rating.repository.RatingRepository;
import eu.renovaro.rating.repository.ReviewRatingCriteriaRepository;
import eu.renovaro.rating.service.RatingService;
import eu.renovaro.reservation.domain.ReStatusCode;
import eu.renovaro.reservation.domain.entity.AdServiceTimeSlot;
import eu.renovaro.reservation.domain.entity.FreelanceRequest;
import eu.renovaro.reservation.domain.entity.ServiceReservation;
import eu.renovaro.reservation.repository.FreelanceRequestRepository;
import eu.renovaro.reservation.repository.ServiceReservationRepository;
import eu.renovaro.user.domain.entity.City;
import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.domain.entity.UserProfile;
import eu.renovaro.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ServiceReservationRepository serviceReservationRepository;
    private final UserProfileRepository userProfileRepository;
    private final ReviewRatingCriteriaRepository reviewRatingCriteriaRepository;
    private final CloudinaryService cloudinaryService;
    private final FreelanceRequestRepository freelanceRequestRepository;

    @Override
    public void createReview(Long userId, CreateReviewRequest request, MultipartFile file) {
        if (request == null || request.getReservationId() == null) {
            throw new IllegalArgumentException();
        }

                 ServiceReservation reservation = serviceReservationRepository
                    .findById(request.getReservationId())
                    .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));


        if (reservation.getUser() == null
                || reservation.getUser().getUserId() == null
                || !reservation.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("Reservation does not belong to current user");
        }

        if (reservation.getReview() != null) {
            throw new IllegalStateException("Review already exists for this reservation");
        }

        ReStatusCode statusCode = reservation.getReStatus() != null
                ? reservation.getReStatus().getReStatusCode()
                : null;

        if (statusCode != ReStatusCode.COMPLETED
                || !reservation.getServiceTimeSlot().getDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Reservation is not completed");
        }

        Review review = new Review();
        review.setServiceReservation(reservation);
        review.setCreatedAt(LocalDate.now());
        review.setReviewContent(request.getReviewContent());
        review.setServicePrice(request.getServicePrice());
        review.setServiceStartDate(request.getServiceStartDate());
        review.setServiceEndDate(request.getServiceEndDate());
        review.setRatingGeneral(request.getRatingGeneral());

        if (file != null && !file.isEmpty()) {
            CloudinaryService.UploadResult uploadResult = cloudinaryService.uploadImage(file);
            review.setReviewImageUrl(uploadResult.url());
        }

        Review savedReview = ratingRepository.save(review);

        if (request.getCriteriaRatings() != null) {
            request.getCriteriaRatings().stream()
                    .filter(c -> c.getRatingCriteriaId() != null && c.getRating() != null)
                    .map(c -> {
                        ReviewRatingCriteria entity = new ReviewRatingCriteria();
                        entity.setReviewId(savedReview.getReviewId());
                        entity.setRatingCriteriaId(c.getRatingCriteriaId());
                        entity.setRatingByCriteria(c.getRating().longValue());
                        return entity;
                    })
                    .forEach(reviewRatingCriteriaRepository::save);
        }

        if (reservation.getServiceTimeSlot() != null
                && reservation.getServiceTimeSlot().getUserProfile() != null
                && request.getRatingGeneral() != null) {

            UserProfile profile = reservation.getServiceTimeSlot().getUserProfile();

            int oldCount = profile.getRatingCount() != null ? profile.getRatingCount() : 0;
            double oldAvg = profile.getRatingAverage() != null ? profile.getRatingAverage() : 0.0;

            int newCount = oldCount + 1;
            double newAvg = (oldAvg * oldCount + request.getRatingGeneral()) / newCount;

            profile.setRatingCount(newCount);
            profile.setRatingAverage(newAvg);
            userProfileRepository.save(profile);
        }
        System.out.println("statusCode = " + statusCode);
        System.out.println("date = " + reservation.getServiceTimeSlot().getDate());
        System.out.println("today = " + LocalDate.now());
    }


    @Override
    public UserRatingDto getRatingBreakdownByUserProfileId(Long userProfileId) {
        Map<Integer, Long> stars = new HashMap<>();

        for (int i = 1; i <= 5; i++) stars.put(i, 0L);

        Stream.concat(
                ratingRepository.countStarsFreelance(userProfileId).stream(),
                ratingRepository.countStarsLocalFlex(userProfileId).stream()
        ).forEach(r -> {
            Integer star = r[0] != null ? ((Number) r[0]).intValue() : null;
            Long count = r[1] != null ? ((Number) r[1]).longValue() : 0L;

            if (star != null) {
                stars.merge(star, count, Long::sum);
            }
        });

        List<RatingCriteriaDto> criteria =
                Stream.concat(
                                ratingRepository.avgCriteriaFreelance(userProfileId).stream(),
                                ratingRepository.avgCriteriaLocalFlex(userProfileId).stream()
                        )
                        .collect(Collectors.groupingBy(
                                r -> (String) r[0],
                                Collectors.averagingDouble(r -> ((Double) r[1]))
                        ))
                        .entrySet().stream()
                        .map(e -> new RatingCriteriaDto(e.getKey(), e.getValue()))
                        .toList();

        UserRatingDto dto = new UserRatingDto();
        dto.setStars(stars);
        dto.setCriteria(criteria);
        return dto;
    }

    @Override
    public List<ReviewDto> getPublicReviewsByUserProfileId(Long userProfileId) {
        return fetchSortedReviews(userProfileId).stream()
                .map(r -> toDto(r, true))
                .toList();
    }

    @Override
    public List<ReviewDto> getPrivateReviewsByUserProfileId(Long userProfileId) {
        return fetchSortedReviews(userProfileId).stream()
                .map(r -> toDto(r, false))
                .toList();
    }

    private List<Review> fetchSortedReviews(Long userProfileId) {
        List<Review> freelance = ratingRepository.findFreelanceReviewsByUserProfileId(userProfileId);
        List<Review> localFlex = ratingRepository.findLocalFlexReviewsByUserProfileId(userProfileId);

        return Stream.concat(freelance.stream(), localFlex.stream())
                .sorted(Comparator
                        .comparing(Review::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Review::getReviewId, Comparator.nullsLast(Comparator.reverseOrder()))
                )
                .toList();
    }

    private ReviewDto toDto(Review r, boolean isPublic) {
        ReviewDto dto = new ReviewDto();

        dto.setUserReviewId(r.getReviewId());
        dto.setCreatedAt(r.getCreatedAt() != null ? r.getCreatedAt().toString() : null);
        dto.setUserReviewContent(r.getReviewContent());
        dto.setReviewImageUrl(r.getReviewImageUrl());
        dto.setServicePrice(r.getServicePrice());
        dto.setRatingGeneral(r.getRatingGeneral() != null ? r.getRatingGeneral().intValue() : null);
        dto.setProviderAnswer(r.getProviderAnswer());
        dto.setServiceDurationDays(calcDurationDays(r.getServiceStartDate(), r.getServiceEndDate()));

        User reviewer = null;
        User provider = null;
        String city = null;

        if (r.getFreelanceRequest() != null) {
            reviewer = r.getFreelanceRequest().getUser();

            Ad ad = r.getFreelanceRequest().getFreelancePackage().getAd();
            provider = (ad != null && ad.getUserProfile() != null) ? ad.getUserProfile().getUser() : null;

            city = cityName(ad != null ? ad.getCity() : null);
        } else if (r.getServiceReservation() != null) {
            reviewer = r.getServiceReservation().getUser();

            provider = (r.getServiceReservation().getServiceTimeSlot() != null
                    && r.getServiceReservation().getServiceTimeSlot().getUserProfile() != null)
                    ? r.getServiceReservation().getServiceTimeSlot().getUserProfile().getUser()
                    : null;

            city = (r.getServiceReservation().getServiceTimeSlot() != null)
                    ? r.getServiceReservation().getServiceTimeSlot().getAdServiceTimeSlotList().stream()
                    .map(AdServiceTimeSlot::getAd)
                    .filter(Objects::nonNull)
                    .map(Ad::getCity)
                    .filter(Objects::nonNull)
                    .map(City::getCityName)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null)
                    : null;
        }

        fillProvider(dto, provider);
        fillReviewer(dto, reviewer, isPublic);
        dto.setServiceCity(city);

        return dto;
    }

    private Integer calcDurationDays(LocalDate start, LocalDate end) {
        return (start != null && end != null) ? (int) ChronoUnit.DAYS.between(start, end) : null;
    }

    private void fillProvider(ReviewDto dto, User provider) {
        dto.setProviderName(fullName(provider));
        dto.setProviderProfileImageUrl(provider != null ? provider.getProfileImageUrl() : null);
    }

    private void fillReviewer(ReviewDto dto, User reviewer, boolean isPublic) {
        if (!isPublic) {
            dto.setReviewerName(fullName(reviewer));
            dto.setReviewerProfileImageUrl(reviewer != null ? reviewer.getProfileImageUrl() : null);
            return;
        }

        String first = reviewer != null ? reviewer.getFirstName() : null;
        String last = reviewer != null ? reviewer.getLastName() : null;

        String reviewerName = publicReviewerName(first, last);
        String reviewerInitials = publicReviewerInitials(first, last);

        dto.setReviewerName(reviewerName);
        dto.setReviewerProfileImageUrl(reviewerInitials);
    }

    private String publicReviewerName(String first, String last) {
        String f = safeTrim(first);
        String l = safeTrim(last);

        if (f == null && l == null) return null;
        if (f == null) return firstLetter(l) + ".";
        if (l == null) return f;

        return f + " " + firstLetter(l) + ".";
    }

    private String publicReviewerInitials(String first, String last) {
        String f = safeTrim(first);
        String l = safeTrim(last);

        String i1 = firstLetter(f);
        String i2 = firstLetter(l);

        return (i1 + i2).isBlank() ? null : (i1 + i2);
    }

    private String fullName(User u) {
        if (u == null) return null;

        String first = safeTrim(u.getFirstName());
        String last = safeTrim(u.getLastName());

        if (first == null && last == null) return null;
        if (first == null) return last;
        if (last == null) return first;

        return first + " " + last;
    }

    private String cityName(City city) {
        return city != null ? city.getCityName() : null;
    }

    private String safeTrim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private String firstLetter(String s) {
        String t = safeTrim(s);
        return (t == null) ? "" : t.substring(0, 1);
    }

}
