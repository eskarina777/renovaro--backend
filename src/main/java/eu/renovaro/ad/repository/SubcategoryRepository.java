package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.entity.Ad;
import eu.renovaro.ad.domain.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SubcategoryRepository  extends JpaRepository<Subcategory, Long>, JpaSpecificationExecutor<Ad> {
    List<Subcategory> findByCategory_CategoryIdOrderBySubcategoryIdAsc(Long categoryId);
}
