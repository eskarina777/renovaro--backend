package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
