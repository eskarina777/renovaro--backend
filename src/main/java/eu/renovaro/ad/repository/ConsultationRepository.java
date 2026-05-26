package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    public Optional<Consultation> findByLocalService_LocalServiceId(Long localServiceId);
}
