package eu.renovaro.reservation.domain.entity;

import eu.renovaro.rating.domain.entity.Review;
import eu.renovaro.user.domain.entity.Address;
import jakarta.persistence.*;
import lombok.*;
import eu.renovaro.user.domain.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_reservation")
public class ServiceReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_reservation_id")
    private Long serviceReservationId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @OneToOne
    @JoinColumn(name = "service_time_slot_id", unique = true)
    private ServiceTimeSlot serviceTimeSlot;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "re_status_id")
    private ReStatus reStatus;

    @OneToOne(mappedBy = "serviceReservation")
    private Review review;
}

