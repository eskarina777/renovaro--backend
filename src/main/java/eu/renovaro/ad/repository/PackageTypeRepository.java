package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.PackageTypeCode;
import eu.renovaro.ad.domain.ServiceTypeCode;
import eu.renovaro.ad.domain.entity.PackageType;
import eu.renovaro.ad.domain.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageTypeRepository extends JpaRepository<PackageType, Long> {
    Optional<PackageType> findByPackageTypeCode(PackageTypeCode packageTypeCode);
}
