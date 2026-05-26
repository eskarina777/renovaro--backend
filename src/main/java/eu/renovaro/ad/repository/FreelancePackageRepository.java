package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.PackageTypeCode;
import eu.renovaro.ad.domain.entity.FreelancePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FreelancePackageRepository extends JpaRepository<FreelancePackage, Long> {

    Optional<FreelancePackage> findByAd_AdIdAndPackageType_PackageTypeCode(Long adId, PackageTypeCode packageTypeCode);
    @Query("""
    SELECT fp
    FROM FreelancePackage fp
    JOIN FETCH fp.packageType pt
    WHERE fp.ad.adId = :adId
""")
    java.util.List<FreelancePackage> getFreelancePanelByAdId(@Param("adId") Long adId);

}

