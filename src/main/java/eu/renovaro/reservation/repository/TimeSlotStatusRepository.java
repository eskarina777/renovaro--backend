package eu.renovaro.reservation.repository;

import eu.renovaro.reservation.domain.TimeSlotStatusCode;
import eu.renovaro.reservation.domain.entity.TimeSlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeSlotStatusRepository extends JpaRepository<TimeSlotStatus, Long> {
    Optional<TimeSlotStatus> findByTimeSlotStatusCode(TimeSlotStatusCode timeSlotStatusCode);
}