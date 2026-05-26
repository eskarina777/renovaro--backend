package eu.renovaro.ad.controller;

import eu.renovaro.ad.domain.*;
import eu.renovaro.ad.service.AdService;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/public/ads")
public class AdController {

    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("/search")
    public AdsPageResponse searchAds(@ModelAttribute AdFilterRequest filter, Pageable pageable) {
        return adService.searchAds(filter, pageable);
    }

    @GetMapping("/subcategory/{id}/filter")
    public AdsPageResponse filterAds(@PathVariable Long id, @ModelAttribute AdFilterRequest filter,
                                     Pageable pageable) {
        return adService.filterAds(id, filter, pageable);
    }

    @GetMapping("/top-subcategories")
    public List<SubcategoryDto> getTopViewedSubcategories() {
        return adService.getTopViewedSubcategories();
    }

    @GetMapping("/top-architecture-design")
    public List<AdCardDto> getTopViewedArchitectureAds() {
        return adService.getTopArchitectureAndDesignAds();
    }

    @GetMapping("/recommended")
    public List<AdCardDto> getRecommendedAds(@RequestParam Long subcategoryId,@RequestParam Long excludeAdId) {
        return adService.getRecommendedAds(subcategoryId, excludeAdId);
    }

    @GetMapping("/ad-details/{id}")
    public AdDetailsResponseDto getAdDetailsById(@PathVariable Long id) {
        return adService.getAdDetailsById(id);
    }

    @GetMapping("/{adId}/freelance-panel")
    public FreelanceResponseDto getFreelancePanel(@PathVariable Long adId) {
        return adService.getFreelancePanel(adId);
    }

    @GetMapping("/pricing-plans")
    public List<AdPricingPlanDto> getPricingPlans() {
        return adService.getAdPricingPlans();
    }
    @GetMapping("/package-types")
    public List<PackageTypeDto> getPackageTypes() {
        return adService.getPackageTypes();
    }
    @GetMapping("/pricing-units")
    public List<PricingUnitDto> getPricingUnits() {
        return adService.getPricingUnits();
    }
}
