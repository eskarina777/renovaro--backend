package eu.renovaro.rating.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserRatingDto {
    private Map<Integer, Long> stars;
    private List<RatingCriteriaDto> criteria;
}
