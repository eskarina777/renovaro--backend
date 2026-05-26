package eu.renovaro.user.domain.entity;

import eu.renovaro.ad.domain.entity.Ad;
import eu.renovaro.reservation.domain.entity.ServiceTimeSlot;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_profile_id")
    private Long userProfileId;

    @Column(name = "user_details")
    private String userDetails;

    @Column(name = "rating_average")
    private Double ratingAverage;

    @Column(name = "rating_count")
    private Integer ratingCount;

    @Column(name = "calendar_limit")
    private LocalDate calendarLimit;

    @Column(name = "credit_balance")
    private Integer creditBalance;

    @Column(name = "user_website")
    private String userWebsite;

    @Column(name = "show_phone_number")
    private Boolean showPhoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "userProfile")
    private Company company;

    @OneToMany(mappedBy = "userProfile")
    private List<ServiceTimeSlot> serviceTimeSlots;

    @OneToMany(mappedBy = "userProfile")
    private List<Ad> ads;

}
