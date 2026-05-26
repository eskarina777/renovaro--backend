package eu.renovaro.ad.service.impl;

import eu.renovaro.ad.domain.*;
import eu.renovaro.ad.domain.entity.*;
import eu.renovaro.ad.mapper.AdMapper;
import eu.renovaro.ad.repository.*;
import eu.renovaro.ad.service.AdService;
import eu.renovaro.ad.util.AdFreelanceSpecification;
import eu.renovaro.ad.util.AdLocalSpecification;
import eu.renovaro.ad.util.AdSearchSpecification;
import eu.renovaro.rating.repository.RatingRepository;
import eu.renovaro.user.domain.RoleName;
import eu.renovaro.user.domain.entity.*;
import eu.renovaro.user.service.UserProfileService;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import static eu.renovaro.common.util.UserNameUtil.constructUserName;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AdServiceImpl implements AdService {

    private static final AdMapper MAPPER = Mappers.getMapper(AdMapper.class);

    private final AdImageRepository adImageRepository;
    private final AdRepository adRepository;
    private final FreelancePackageRepository freelancePackageRepository;
    private final LocalServiceRepository localServiceRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final UserProfileService userProfileService;
    private final AdPricingPlanRepository adPricingPlanRepository;
    private final PackageTypeRepository packageTypeRepository;
    private final PricingUnitRepository pricingUnitRepository;
    public AdServiceImpl(
            AdRepository adRepository,
            RatingRepository userRatingRepository,
            FreelancePackageRepository freelancePackageRepository,
            LocalServiceRepository localServiceRepository,
            SubcategoryRepository subcategoryRepository,
            UserProfileService userProfileService,
            AdImageRepository adImageRepository,
            AdPricingPlanRepository adPricingPlanRepository,
            PackageTypeRepository packageTypeRepository,
            PricingUnitRepository pricingUnitRepository
    ) {
        this.adRepository = adRepository;
        this.freelancePackageRepository = freelancePackageRepository;
        this.localServiceRepository = localServiceRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.userProfileService = userProfileService;
        this.adImageRepository = adImageRepository;
        this.adPricingPlanRepository = adPricingPlanRepository;
        this.packageTypeRepository = packageTypeRepository;
        this.pricingUnitRepository = pricingUnitRepository;
    }

    @Override
    public AdDetailsResponseDto getAdDetailsById(Long adId) {
        Ad ad = adRepository.getAdDetailsById(adId).orElseThrow();

        ServiceTypeCode serviceTypeCode = ad.getServiceType() != null ? ad.getServiceType().getServiceTypeCode() : null;

        SubcategoryDto subcategoryDto = ad.getSubcategory() != null ? MAPPER.map(ad.getSubcategory()) : null;
        CategoryDto categoryDto = (ad.getSubcategory() != null && ad.getSubcategory().getCategory() != null)
                ? MAPPER.map(ad.getSubcategory().getCategory())
                : null;

        Long userProfileId = ad.getUserProfile() != null ? ad.getUserProfile().getUserProfileId() : null;
        eu.renovaro.user.domain.UserProfileDto userProfileDto =
                userProfileId != null ? userProfileService.getUserProfileDto(userProfileId) : null;

        java.util.List<AdImageUrlDto> images = adImageRepository
                .findByAd_AdIdOrderByIsPrimaryDescAdImageIdAsc(ad.getAdId())
                .stream()
                .map(img -> new AdImageUrlDto(img.getAdImageId(), img.getIsPrimary(), img.getAdImageUrl()))
                .toList();

        java.math.BigDecimal priceMin = null;
        java.math.BigDecimal priceMax = null;

        if (serviceTypeCode == ServiceTypeCode.LOCAL_FLEX || serviceTypeCode == ServiceTypeCode.LOCAL_FIXED) {
            LocalService localService = localServiceRepository.findByAd_AdId(ad.getAdId());
            if (localService != null) {
                priceMin = localService.getServicePriceMin();
                priceMax = localService.getServicePriceMax();
            }
        }

        String pricingUnit = ad.getPricingUnit() != null ? ad.getPricingUnit().getPricingUnitLabel() : null;

        AdDetailsDto adDetailsDto = new AdDetailsDto(
                ad.getCity() != null ? ad.getCity().getCityName() : null,
                ad.getTitle(),
                ad.getDescription(),
                priceMin,
                priceMax,
                pricingUnit,
                images,
                ad.getImportantInfo()
        );

        return new AdDetailsResponseDto(
                ad.getAdId(),
                serviceTypeCode,
                subcategoryDto,
                categoryDto,
                userProfileDto,
                adDetailsDto
        );
    }


    @Override
    public List<AdCardDto> getRecommendedAds(Long subcategoryId, Long excludeAdId) {
        List<Ad> ads = adRepository.getRecommendedAds(subcategoryId, excludeAdId);

        return ads.stream()
                .limit(3)
                .map(ad -> toAdCardDto(ad))
                .toList();
    }

    @Override
    public List<AdCardDto> getTopArchitectureAndDesignAds() {
        return adRepository.findTopArchitectureAds()
                .stream()
                .limit(8)
                .map(this::toAdCardDto)
                .toList();
    }

    @Override
    public List<AdCardDto> getMostViewedAdsByCategory(Long categoryId) {
        Pageable top5 = PageRequest.of(0, 5);
        Page<Ad> page = adRepository.findMostViewedActiveByCategory(categoryId, top5);

        return page.getContent().stream()
                .map(this::toAdCardDto)
                .toList();
    }

    @Override
    public AdsPageResponse filterAds(Long subcategoryId, AdFilterRequest filter, Pageable pageable) {

        Subcategory subcategory = subcategoryRepository.findById(subcategoryId)
                .orElseThrow();

        Category category = subcategory.getCategory();

        Page<Ad> page;

        if ("architecture".equals(category.getCategoryCode())) {
            page = adRepository.findAll(
                    AdFreelanceSpecification.matches(filter, subcategoryId),
                    pageable
            );
        } else {
            page = adRepository.findAll(
                    AdLocalSpecification.matches(filter, subcategoryId),
                    pageable
            );
        }

        List<AdCardDto> ads = page.getContent().stream()
                .map(this::toAdCardDto)
                .toList();

        return new AdsPageResponse(ads, page.getTotalElements());
    }


    @Override
    public List<AdDetailsDto> getAdDetails(List<Long> adIds) {
        return null;
    }


    @Override
    public AdsPageResponse searchAds(AdFilterRequest filter, Pageable pageable) {
        Pageable unsorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Ad> page = adRepository.findAll(AdSearchSpecification.matches(filter), unsorted);

        List<AdCardDto> ads = page.getContent().stream()
                .map(this::toAdCardDto)
                .toList();

        return new AdsPageResponse(ads, page.getTotalElements());
    }



    @Override
    public List<SubcategoryDto> getTopViewedSubcategories() {
        Set<Long> seen = new HashSet<>();
        return adRepository.findAllActiveAdsOrderByViewsDesc().stream()
                .filter(ad -> ad.getSubcategory() != null)
                .filter(ad -> seen.add(ad.getSubcategory().getSubcategoryId()))
                .limit(8)
                .map(ad -> MAPPER.map(ad.getSubcategory()))
                .toList();
    }

    private AdCardDto toAdCardDto(Ad ad) {
        UserProfile profile = ad.getUserProfile();
        User user = (profile != null) ? profile.getUser() : null;

        Long userId = (user != null) ? user.getUserId() : null;
        String userName = constructUserName(ad.getUserProfile().getUser());

        String profileImageUrl = (user != null) ? user.getProfileImageUrl() : null;

        ProviderRole userRole = ProviderRole.SPECIALIST;
        if (user != null && user.getUserRoles() != null) {
            boolean isCompany = user.getUserRoles().stream()
                    .map(UserRole::getRole)
                    .filter(Objects::nonNull)
                    .map(r -> r.getRoleName())
                    .anyMatch(RoleName.COMPANY::equals);
            userRole = isCompany ? ProviderRole.COMPANY : ProviderRole.SPECIALIST;
        }

        String city = (ad.getCity() != null) ? ad.getCity().getCityName() : null;

        Double ratingAverage = (profile != null) ? profile.getRatingAverage() : null;
        Integer ratingCount = (profile != null) ? profile.getRatingCount() : null;

        String adImageUrl = adImageRepository.findFirstByAd_AdIdAndIsPrimaryTrue(ad.getAdId())
                .map(img -> img.getAdImageUrl())
                .orElse(null);

        BigDecimal priceMin = null;
        BigDecimal priceMax = null;

        ServiceTypeCode serviceType = (ad.getServiceType() != null) ? ad.getServiceType().getServiceTypeCode() : null;

        if (serviceType == ServiceTypeCode.FREELANCE) {
            BigDecimal basic = freelancePackageRepository
                    .findByAd_AdIdAndPackageType_PackageTypeCode(ad.getAdId(), PackageTypeCode.BASIC)
                    .map(FreelancePackage::getPackagePrice)
                    .orElse(null);
            priceMin = basic;
            priceMax = null;
        } else {
            LocalService localService = localServiceRepository.findByAd_AdId(ad.getAdId());
            if (localService != null) {
                priceMin = localService.getServicePriceMin();
                priceMax = localService.getServicePriceMax();
            }
        }

        String pricingUnit = (ad.getPricingUnit() != null) ? ad.getPricingUnit().getPricingUnitLabel() : null;

        String status = (ad.getAdStatus() != null && ad.getAdStatus().getAdStatusCode() != null)
                ? ad.getAdStatus().getAdStatusCode().name()
                : null;

        return new AdCardDto(
                ad.getAdId(),
                status,
                userId,
                userName,
                profileImageUrl,
                serviceType,
                ad.getTitle(),
                ratingAverage,
                ratingCount,
                priceMin,
                priceMax,
                pricingUnit,
                adImageUrl,
                userRole,
                city
        );
    }


    @Override
    public FreelanceResponseDto getFreelancePanel(Long adId) {
        java.util.List<FreelancePackage> rows = freelancePackageRepository.getFreelancePanelByAdId(adId);

        java.util.Map<PackageTypeCode, FreelancePackageDto> byType = new java.util.EnumMap<>(PackageTypeCode.class);

        for (FreelancePackage fp : rows) {
            PackageTypeCode code = fp.getPackageType() != null ? fp.getPackageType().getPackageTypeCode() : null;
            if (code == null) continue;

            FreelancePackageDto dto = new FreelancePackageDto(
                    fp.getPackageTitle(),
                    fp.getPackageDescription(),
                    fp.getPackagePrice(),
                    fp.getDeliveryDays(),
                    parseIntOrNull(fp.getRevisionCount()),
                    fp.getRenderCount(),
                    fp.getDetailDrawingCount(),
                    fp.getHas3dModel(),
                    fp.getHas2dDrawings(),
                    fp.getHasSourceFile(),
                    fp.getImportantInfo()
            );

            byType.put(code, dto);
        }

        FreelanceResponseDto packages = new FreelanceResponseDto(
                byType.get(PackageTypeCode.BASIC),
                byType.get(PackageTypeCode.STANDARD),
                byType.get(PackageTypeCode.PREMIUM)
        );

        return packages;
    }

    private Integer parseIntOrNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        if (trimmed.isEmpty()) return null;
        try {
            return Integer.valueOf(trimmed);
        } catch (Exception ignored) {
            return null;
        }
    }
    @Override
    public List<AdCardDto> getProviderAdsByEmail(String email) {
        return adRepository.getProviderAdsByEmail(email)
                .stream()
                .map(ad -> toAdCardDto(ad))
                .toList();
    }

    @Override
    public List<AdPricingPlanDto> getAdPricingPlans() {
        return adPricingPlanRepository.findAll().stream()
                .map(MAPPER::map)
                .toList();
    }

    @Override
    public List<PackageTypeDto> getPackageTypes() {
        return packageTypeRepository.findAll().stream()
                .map(MAPPER::map)
                .toList();
    }

    @Override
    public List<PricingUnitDto> getPricingUnits() {
        return pricingUnitRepository.findAll().stream()
                .map(MAPPER::map)
                .toList();
    }

}
