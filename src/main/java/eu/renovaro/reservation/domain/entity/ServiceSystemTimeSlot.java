package eu.renovaro.reservation.domain.entity;

import eu.renovaro.reservation.domain.ServiceSystemTimeSlotId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "service_system_time_slot")
@IdClass(ServiceSystemTimeSlotId.class)
public class ServiceSystemTimeSlot {

    @Id
    @Column(name = "system_time_slot_id", nullable = false)
    private Long systemTimeSlotId;

    @Id
    @Column(name = "service_time_slot_id", nullable = false)
    private Long serviceTimeSlotId;

    @ManyToOne
    @JoinColumn(name = "system_time_slot_id", referencedColumnName = "system_time_slot_id", insertable = false, updatable = false)
    private SystemTimeSlot systemTimeSlot;

    @ManyToOne
    @JoinColumn(name = "service_time_slot_id", referencedColumnName = "service_time_slot_id", insertable = false, updatable = false)
    private ServiceTimeSlot serviceTimeSlot;

    public ServiceSystemTimeSlot(Long systemTimeSlotId, Long serviceTimeSlotId) {
        this.systemTimeSlotId = systemTimeSlotId;
        this.serviceTimeSlotId = serviceTimeSlotId;
    }

}
