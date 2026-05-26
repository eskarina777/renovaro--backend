package eu.renovaro.notification.repository;

import eu.renovaro.notification.domain.EntityTypeName;
import eu.renovaro.notification.domain.entity.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntityTypeRepository extends JpaRepository<EntityType, Long> {

    Optional<EntityType> findByEntityTypeName(EntityTypeName entityTypeName);
}