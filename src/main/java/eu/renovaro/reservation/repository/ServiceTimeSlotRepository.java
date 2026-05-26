package eu.renovaro.reservation.repository;

import eu.renovaro.reservation.domain.entity.AdServiceTimeSlot;
import eu.renovaro.reservation.domain.entity.ServiceTimeSlot;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ServiceTimeSlotRepository extends JpaRepository<ServiceTimeSlot, Long> {

    @Query("""
            SELECT sts
            FROM ServiceTimeSlot sts
            JOIN sts.adServiceTimeSlotList ast
            WHERE ast.adId = :adId
            ORDER BY sts.date ASC,
                     sts.startTimeLabel ASC,
                     sts.serviceTimeSlotId ASC
            """)
    List<ServiceTimeSlot> findSlotsByAdId(@Param("adId") Long adId);

    @Query("""
        SELECT DISTINCT sts
        FROM ServiceTimeSlot sts
        LEFT JOIN FETCH sts.timeSlotStatus tss
        LEFT JOIN FETCH sts.serviceSystemTimeSlotList ssts
        WHERE sts.userProfile.userProfileId = :userProfileId
          AND sts.date >= :startDate
          AND sts.date <= :endDate
        ORDER BY sts.date ASC,
                 sts.serviceTimeSlotId ASC
    """)
    List<ServiceTimeSlot> findProviderSlotsBetween(
            @Param("userProfileId") Long userProfileId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}


