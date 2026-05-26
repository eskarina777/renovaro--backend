package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.entity.AdImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdImageRepository extends JpaRepository<AdImage, Long> {

    Optional<AdImage> findFirstByAd_AdIdAndIsPrimaryTrue(Long adId);
    List<AdImage> findByAd_AdIdOrderByIsPrimaryDescAdImageIdAsc(Long adId);
}
