package eu.renovaro.rating.domain.entity;

import eu.renovaro.rating.domain.ReviewCriteriaId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "review_rating_criteria")
@IdClass(ReviewCriteriaId.class)
public class ReviewRatingCriteria {

    @Id
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Id
    @Column(name = "rating_criteria_id", nullable = false)
    private Long ratingCriteriaId;

    @Column(name = "rating_by_criteria", nullable = false)
    private Long ratingByCriteria;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "review_id", insertable = false, updatable = false)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "rating_criteria_id", referencedColumnName = "rating_criteria_id", insertable = false, updatable = false)
    private RatingCriteria ratingCriteria;

    public ReviewRatingCriteria(Long reviewId, Long ratingCriteriaId) {
        this.reviewId = reviewId;
        this.ratingCriteriaId = ratingCriteriaId;
    }
}

