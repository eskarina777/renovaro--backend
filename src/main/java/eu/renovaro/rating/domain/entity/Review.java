package eu.renovaro.rating.domain.entity;

import eu.renovaro.reservation.domain.entity.FreelanceRequest;
import eu.renovaro.reservation.domain.entity.ServiceReservation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "review_content")
    private String reviewContent;

    @Column(name = "service_start_date")
    private LocalDate serviceStartDate;

    @Column(name = "service_end_date")
    private LocalDate serviceEndDate;

    @Column(name = "service_price")
    private Double servicePrice;

    @Column(name = "rating_general")
    private Integer ratingGeneral;

    @Column(name = "review_image_url")
    private String reviewImageUrl;

    @Column(name = "review_image_id")
    private String reviewImageId;

    @Column(name = "provider_answer")
    private String providerAnswer;

    @OneToOne
    @JoinColumn(name = "service_reservation_id")
    private ServiceReservation serviceReservation ;

    @OneToOne
    @JoinColumn(name = "freelance_request_id")
    private FreelanceRequest freelanceRequest ;

    @OneToMany(mappedBy = "review")
    private List<ReviewRatingCriteria> ratingCriteriaList;

}
