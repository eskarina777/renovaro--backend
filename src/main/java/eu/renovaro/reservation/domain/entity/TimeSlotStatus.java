package eu.renovaro.reservation.domain.entity;

import eu.renovaro.reservation.domain.TimeSlotStatusCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_slot_status")
public class TimeSlotStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_slot_status_id")
    private Long timeSlotStatusId;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_slot_status_Code")
    private TimeSlotStatusCode timeSlotStatusCode;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "timeSlotStatus")
    private List<ServiceTimeSlot> serviceTimeSlotList;
}

