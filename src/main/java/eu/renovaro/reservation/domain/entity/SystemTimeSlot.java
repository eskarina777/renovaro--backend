package eu.renovaro.reservation.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "system_time_slot")
public class SystemTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_time_slot_id")
    private Long systemTimeSlotId;

    @Column(name = "time_slot")
    private LocalTime timeSlot;

    @Column(name = "time_slot_label")
    private String timeSlotLabel;

    @OneToMany(mappedBy = "systemTimeSlot")
    private List<ServiceSystemTimeSlot> serviceSystemTimeSlotList;

}
