package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.entity.ClientServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestRepository extends JpaRepository<ClientServiceRequest, Long> {
}