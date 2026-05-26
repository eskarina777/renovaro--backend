package eu.renovaro.reservation.repository;


import eu.renovaro.reservation.domain.entity.AdServiceTimeSlot;
import eu.renovaro.reservation.domain.AdServiceTimeSlotId;
import eu.renovaro.reservation.domain.entity.ServiceTimeSlot;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdServiceTimeSlotRepository extends JpaRepository<AdServiceTimeSlot, AdServiceTimeSlotId> {



    @Query("""
        SELECT sts
        FROM AdServiceTimeSlot ast
        JOIN ast.serviceTimeSlot sts
        WHERE ast.adId = :adId
        ORDER BY sts.date ASC, sts.startTimeLabel ASC, sts.serviceTimeSlotId ASC
    """)
    List<ServiceTimeSlot> findSlotsByAdId(@Param("adId") Long adId);

}
