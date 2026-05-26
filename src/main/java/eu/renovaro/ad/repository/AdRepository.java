package eu.renovaro.ad.repository;

import eu.renovaro.ad.domain.AdStatusCode;
import eu.renovaro.ad.domain.entity.Ad;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
public interface AdRepository extends JpaRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {
    List<Ad> findByAdExpirationDateBetween(LocalDate from, LocalDate to);

    @Query("""
    SELECT a
    FROM Ad a
    JOIN FETCH a.userProfile up
    JOIN FETCH a.subcategory s
    JOIN FETCH s.category
    JOIN FETCH a.serviceType
    LEFT JOIN FETCH a.city
    LEFT JOIN FETCH a.pricingUnit
    WHERE a.adId = :adId
""")
    java.util.Optional<Ad> getAdDetailsById(@Param("adId") Long adId);



    @Query("""
    SELECT a
    FROM Ad a
    WHERE a.subcategory.subcategoryId = :subcategoryId
      AND a.adId <> :excludeAdId
      AND a.adStatus.adStatusCode = eu.renovaro.ad.domain.AdStatusCode.ACTIVE
    ORDER BY a.createdAt DESC, a.adId DESC
""")
    List<Ad> getRecommendedAds(
            Long subcategoryId,
            Long excludeAdId
    );


    @Query("""
    SELECT a
    FROM Ad a
    JOIN a.subcategory s
    JOIN s.category c
    WHERE c.categoryCode = 'architecture'
      AND a.adStatus.adStatusCode = eu.renovaro.ad.domain.AdStatusCode.ACTIVE
    ORDER BY a.createdAt DESC, a.adId DESC
    """)
    List<Ad> findTopArchitectureAds();

    @Query("""
    SELECT a
    FROM Ad a
    JOIN a.subcategory s
    JOIN s.category c
    WHERE c.categoryId = :categoryId
      AND a.adStatus.adStatusCode = eu.renovaro.ad.domain.AdStatusCode.ACTIVE
    ORDER BY COALESCE(a.viewCount, 0) DESC, a.createdAt DESC, a.adId DESC
    """)
    Page<Ad> findMostViewedActiveByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("""
    SELECT a FROM Ad a
    WHERE a.adStatus.adStatusCode = 'ACTIVE'
    ORDER BY a.viewCount DESC
    """)
    List<Ad> findAllActiveAdsOrderByViewsDesc();

   Page<Ad> findAll(Specification<Ad> spec, Pageable pageable);

    @Query("""
    SELECT a
    FROM Ad a
    JOIN a.userProfile up
    JOIN up.user u
    WHERE u.email = :email
    ORDER BY
      CASE
        WHEN a.adStatus.adStatusCode = eu.renovaro.ad.domain.AdStatusCode.ACTIVE THEN 0
        ELSE 1
      END,
      a.createdAt DESC,
      a.adId DESC
""")
    List<Ad> getProviderAdsByEmail(@Param("email") String email);

}
