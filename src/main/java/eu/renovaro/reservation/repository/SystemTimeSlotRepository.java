package eu.renovaro.reservation.repository;

import eu.renovaro.reservation.domain.entity.SystemTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SystemTimeSlotRepository extends JpaRepository<SystemTimeSlot, Long> {

    @Query("""
        SELECT sts
        FROM SystemTimeSlot sts
        ORDER BY sts.timeSlot ASC,
                 sts.systemTimeSlotId ASC
    """)
    List<SystemTimeSlot> findAllOrdered();
}
