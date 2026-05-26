package eu.renovaro.reservation.domain;

import java.io.Serializable;
import java.util.Objects;

public class AdServiceTimeSlotId implements Serializable {

    private Long adId;
    private Long serviceTimeSlotId;

    public AdServiceTimeSlotId() {}

    public AdServiceTimeSlotId(Long adId, Long serviceTimeSlotId) {
        this.adId = adId;
        this.serviceTimeSlotId = serviceTimeSlotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || org.hibernate.Hibernate.getClass(this) != org.hibernate.Hibernate.getClass(o)) return false;
        AdServiceTimeSlotId that = (AdServiceTimeSlotId) o;
        return Objects.equals(adId, that.adId) &&
                Objects.equals(serviceTimeSlotId, that.serviceTimeSlotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adId, serviceTimeSlotId);
    }
}
