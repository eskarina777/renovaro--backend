package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.entity.LocalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface LocalServiceRepository extends JpaRepository<LocalService, Integer> {

    LocalService findByAd_AdId(Long adId);

}
