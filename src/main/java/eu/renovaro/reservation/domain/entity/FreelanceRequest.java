package eu.renovaro.reservation.domain.entity;

import eu.renovaro.ad.domain.entity.FreelancePackage;
import eu.renovaro.rating.domain.entity.Review;
import eu.renovaro.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "freelance_request")
public class FreelanceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "freelance_request_id")
    private Long freelanceRequestId;

    @ManyToOne
    @JoinColumn(name = "re_status_id", nullable = false)
    private ReStatus reStatus;

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private FreelancePackage freelancePackage;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @OneToOne(mappedBy = "freelanceRequest")
    private Review review;
}
