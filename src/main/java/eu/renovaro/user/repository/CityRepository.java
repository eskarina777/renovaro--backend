package eu.renovaro.user.repository;

import eu.renovaro.user.domain.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
}