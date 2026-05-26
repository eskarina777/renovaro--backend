package eu.renovaro.ad.service;

import eu.renovaro.ad.domain.*;

import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AdService {
    List<AdDetailsDto> getAdDetails(List<Long> adIds);

    AdsPageResponse searchAds(AdFilterRequest filter, Pageable pageable);
    AdsPageResponse filterAds(Long subcategoryId, AdFilterRequest filter, Pageable pageable);

    List<SubcategoryDto> getTopViewedSubcategories();
    List<AdCardDto> getMostViewedAdsByCategory(Long categoryId);
    List<AdCardDto> getTopArchitectureAndDesignAds();
    List<AdCardDto> getRecommendedAds(Long subcategoryId, Long excludeAdId);
    AdDetailsResponseDto getAdDetailsById(Long adId);
    FreelanceResponseDto getFreelancePanel(Long adId);
    List<AdCardDto> getProviderAdsByEmail(String email);
    List<AdPricingPlanDto> getAdPricingPlans();
    List<PackageTypeDto> getPackageTypes();
    List<PricingUnitDto> getPricingUnits();

}
