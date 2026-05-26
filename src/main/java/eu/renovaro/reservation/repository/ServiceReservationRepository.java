package eu.renovaro.reservation.repository;

import eu.renovaro.reservation.domain.entity.ServiceReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceReservationRepository extends JpaRepository<ServiceReservation, Long> {
    @Query("""
    SELECT DISTINCT sr
    FROM ServiceReservation sr
    JOIN FETCH sr.reStatus rs
    JOIN FETCH sr.user client
    JOIN FETCH sr.address adr
    JOIN FETCH adr.city cty
    JOIN FETCH sr.serviceTimeSlot sts
    JOIN FETCH sts.userProfile up
    JOIN sts.adServiceTimeSlotList asts
    JOIN asts.ad a
    LEFT JOIN FETCH a.localService ls
    LEFT JOIN FETCH ls.consultation c
    WHERE up.userProfileId = :userProfileId
""")
    List<ServiceReservation> findProviderConsultations(@Param("userProfileId") Long userProfileId);


}
