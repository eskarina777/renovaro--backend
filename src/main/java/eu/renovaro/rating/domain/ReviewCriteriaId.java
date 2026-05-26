package eu.renovaro.rating.domain;

import java.io.Serializable;
import java.util.Objects;

public class ReviewCriteriaId implements Serializable {

    private Long reviewId;
    private Long ratingCriteriaId;

    public ReviewCriteriaId() {}

    public ReviewCriteriaId(Long reviewId, Long ratingCriteriaId) {
        this.reviewId = reviewId;
        this.ratingCriteriaId = ratingCriteriaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || org.hibernate.Hibernate.getClass(this) != org.hibernate.Hibernate.getClass(o)) return false;
        ReviewCriteriaId that = (ReviewCriteriaId) o;
        return Objects.equals(reviewId, that.reviewId) &&
                Objects.equals(ratingCriteriaId, that.ratingCriteriaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, ratingCriteriaId);
    }
}

