package eu.renovaro.reservation.domain;

import java.io.Serializable;
import java.util.Objects;

public class ServiceSystemTimeSlotId implements Serializable {

    private Long systemTimeSlotId;
    private Long serviceTimeSlotId;

    public ServiceSystemTimeSlotId() {}

    public ServiceSystemTimeSlotId(Long systemTimeSlot, Long serviceTimeSlot) {
        this.serviceTimeSlotId = systemTimeSlot;
        this.serviceTimeSlotId = serviceTimeSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || org.hibernate.Hibernate.getClass(this) != org.hibernate.Hibernate.getClass(o)) return false;
        ServiceSystemTimeSlotId that = (ServiceSystemTimeSlotId) o;
        return Objects.equals(systemTimeSlotId, that.systemTimeSlotId) &&
                Objects.equals(serviceTimeSlotId, that.serviceTimeSlotId);
    }


    @Override
    public int hashCode() {
        return Objects.hash(systemTimeSlotId, serviceTimeSlotId);
    }
}

