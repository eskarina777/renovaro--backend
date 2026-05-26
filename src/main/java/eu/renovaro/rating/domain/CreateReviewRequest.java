package eu.renovaro.rating.domain;

import eu.renovaro.ad.domain.ServiceTypeCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    private ServiceTypeCode serviceTypeCode;
    private Long reservationId;
    private Integer ratingGeneral;
    private String reviewContent;
    private Double servicePrice;
    private LocalDate serviceStartDate;
    private LocalDate serviceEndDate;

    private List<ReviewCriteriaRatingRequest> criteriaRatings;
}
