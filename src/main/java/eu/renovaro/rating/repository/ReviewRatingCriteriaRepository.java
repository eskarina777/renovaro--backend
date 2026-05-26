package eu.renovaro.rating.repository;

import eu.renovaro.rating.domain.ReviewCriteriaId;
import eu.renovaro.rating.domain.entity.ReviewRatingCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRatingCriteriaRepository
        extends JpaRepository<ReviewRatingCriteria, ReviewCriteriaId> {
}