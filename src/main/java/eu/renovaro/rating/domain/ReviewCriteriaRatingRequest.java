package eu.renovaro.rating.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCriteriaRatingRequest {
    private Long ratingCriteriaId;
    private Integer rating;
}
