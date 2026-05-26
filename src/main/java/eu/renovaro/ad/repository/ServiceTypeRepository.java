package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.ServiceTypeCode;
import eu.renovaro.ad.domain.entity.AdStatus;
import eu.renovaro.ad.domain.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    Optional<ServiceType> findByServiceTypeCode(ServiceTypeCode serviceTypeCode);
}
