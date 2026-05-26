package eu.renovaro.reservation.repository;

import eu.renovaro.reservation.domain.ReStatusCode;
import eu.renovaro.reservation.domain.entity.ReStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReStatusRepository extends JpaRepository<ReStatus, Long> {
    Optional<ReStatus> findByReStatusCode(ReStatusCode code);
}
