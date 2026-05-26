package eu.renovaro.rating.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rating_criteria")
public class RatingCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_criteria_id")
    private Long ratingCriteriaId;

    @Column(name = "rating_criteria_code")
    private String ratingCriteriaName;

    @Column(name = "rating_criteria_label")
    private String ratingCriteriaLabel;

    @OneToMany(mappedBy = "ratingCriteria")
    private List<ReviewRatingCriteria> reviewRatingCriteriaList;
}
