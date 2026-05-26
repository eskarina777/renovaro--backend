package eu.renovaro.rating.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long userReviewId;

    private String createdAt;
    private String userReviewContent;
    private String reviewImageUrl;
    private Double servicePrice;
    private Integer ratingGeneral;
    private String providerAnswer;

    private String reviewerName;
    private String reviewerProfileImageUrl;

    private String providerName;
    private String providerProfileImageUrl;

    private String serviceCity;
    private Integer serviceDurationDays;
}
