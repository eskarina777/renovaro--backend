package eu.renovaro.reservation.domain.entity;

import eu.renovaro.ad.domain.entity.Ad;
import eu.renovaro.reservation.domain.AdServiceTimeSlotId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ad_service_time_slot")
@IdClass(AdServiceTimeSlotId.class)
public class AdServiceTimeSlot {

    @Id
    @Column(name = "ad_id", nullable = false)
    private Long adId;

    @Id
    @Column(name = "service_time_slot_id", nullable = false)
    private Long serviceTimeSlotId;

    @ManyToOne
    @JoinColumn(name = "ad_id", referencedColumnName = "ad_id", insertable = false, updatable = false)
    private Ad ad;

    @ManyToOne
    @JoinColumn(name = "service_time_slot_id", referencedColumnName = "service_time_slot_id", insertable = false, updatable = false)
    private ServiceTimeSlot serviceTimeSlot;

    public AdServiceTimeSlot(Long adId, Long serviceTimeSlotId) {
        this.adId = adId;
        this.serviceTimeSlotId = serviceTimeSlotId;
    }
}
