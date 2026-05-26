package eu.renovaro.reservation.repository;

import eu.renovaro.reservation.domain.entity.FreelanceRequest;
import eu.renovaro.reservation.domain.entity.ReStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelanceRequestRepository extends JpaRepository<FreelanceRequest, Long> {
}
