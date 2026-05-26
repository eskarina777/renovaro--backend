package eu.renovaro.reservation.repository;

import eu.renovaro.reservation.domain.entity.ServiceSystemTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceSystemTimeSlotRepository  extends JpaRepository<ServiceSystemTimeSlot, Long> {
}
