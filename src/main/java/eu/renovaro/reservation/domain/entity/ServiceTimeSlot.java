package eu.renovaro.reservation.domain.entity;

import eu.renovaro.user.domain.entity.UserProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_time_slot")
public class ServiceTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_time_slot_id")
    private Long serviceTimeSlotId;

    @Column(name = "start_time_label")
    private String startTimeLabel;

    @Column(name = "end_time_label")
    private String endTimeLabel;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "time_slot_status_id")
    private TimeSlotStatus timeSlotStatus;

    @OneToMany(mappedBy = "serviceTimeSlot")
    private List<AdServiceTimeSlot> adServiceTimeSlotList;

    @OneToMany(mappedBy = "serviceTimeSlot")
    private List<ServiceSystemTimeSlot> serviceSystemTimeSlotList;

    @OneToOne(mappedBy = "serviceTimeSlot", fetch = FetchType.LAZY)
    private ServiceReservation serviceReservation;
}
