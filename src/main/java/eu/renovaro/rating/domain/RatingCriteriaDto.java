package eu.renovaro.rating.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingCriteriaDto {
    private String label;
    private Double avg;
}
