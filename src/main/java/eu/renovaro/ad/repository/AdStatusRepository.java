package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.AdStatusCode;
import eu.renovaro.ad.domain.entity.AdStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdStatusRepository extends JpaRepository<AdStatus, Long> {

    Optional<AdStatus> findByAdStatusCode(AdStatusCode adStatusCode);
}