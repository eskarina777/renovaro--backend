package eu.renovaro.ad.service.impl;

import eu.renovaro.ad.domain.*;
import eu.renovaro.ad.domain.entity.*;
import eu.renovaro.ad.repository.*;
import eu.renovaro.ad.service.AdPublishService;
import eu.renovaro.common.service.CloudinaryService;
import eu.renovaro.notification.domain.EntityTypeName;
import eu.renovaro.notification.domain.NotificationTypeName;
import eu.renovaro.notification.service.NotificationService;
import eu.renovaro.reservation.domain.CalendarTimeSlotPayload;
import eu.renovaro.reservation.domain.entity.AdServiceTimeSlot;
import eu.renovaro.reservation.domain.entity.ServiceSystemTimeSlot;
import eu.renovaro.reservation.domain.entity.ServiceTimeSlot;
import eu.renovaro.reservation.domain.entity.TimeSlotStatus;
import eu.renovaro.reservation.repository.*;
import eu.renovaro.user.domain.entity.City;
import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.domain.entity.UserProfile;
import eu.renovaro.user.repository.CityRepository;
import eu.renovaro.user.repository.UserProfileRepository;
import eu.renovaro.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdPublishServiceImpl implements AdPublishService {

    private final AdRepository adRepository;
    private final AdStatusRepository adStatusRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final AdPricingPlanRepository adPricingPlanRepository;
    private final CityRepository cityRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final UserProfileRepository  userProfileRepository;
    private final PricingUnitRepository pricingUnitRepository;
    private final ServiceTimeSlotRepository serviceTimeSlotRepository;
    private final TimeSlotStatusRepository timeSlotStatusRepository;
    private final ServiceSystemTimeSlotRepository serviceSystemTimeSlotRepository;
    private final AdServiceTimeSlotRepository adServiceTimeSlotRepository;
    private final LocalServiceRepository localServiceRepository;
    private final ConsultationRepository consultationRepository ;
    private final FreelancePackageRepository  freelancePackageRepository;
    private final PackageTypeRepository  packageTypeRepository;
    private final CloudinaryService cloudinaryService;
    private final AdImageRepository adImageRepository;
    private final NotificationService notificationService;
    private final ServiceRequestRepository serviceRequestRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Long publishAd(PublishAdRequest request, Long userProfileId) {

        City city = null;
        if (request.getAd().getCityId() != null) {
            city = cityRepository.findById(request.getAd().getCityId()).orElseThrow();
        }
        PricingUnit pricingUnit = pricingUnitRepository.findById(request.getAd().getPricingUnitId()).orElseThrow();
        if(pricingUnit.getPricingUnitCode().equals(PricingUnitCode.FINAL)) {
            pricingUnit = null;
        }
        UserProfile userProfile = userProfileRepository.findById(userProfileId).orElseThrow();
        AdStatus adStatus = adStatusRepository.findByAdStatusCode(AdStatusCode.ACTIVE).orElseThrow();
        AdPricingPlan adPricingPlan = adPricingPlanRepository.findById(request.getAd().getAdPricingPlanId()).orElseThrow();
        ServiceType serviceType = serviceTypeRepository.findByServiceTypeCode(request.getAd().getServiceTypeCode()).orElseThrow();
        Subcategory subcategory = subcategoryRepository.findById(request.getAd().getSubcategoryId()).orElseThrow();

        Ad ad = new Ad();
        ad.setCity(city);
        ad.setUserProfile(userProfile);
        ad.setAdStatus(adStatus);
        ad.setAdPricingPlan(adPricingPlan);
        ad.setServiceType(serviceType);
        ad.setPricingUnit(pricingUnit);
        ad.setSubcategory(subcategory);

        ad.setCreatedAt(LocalDateTime.now());
        ad.setAdExpirationDate(calculateExpiration(adPricingPlan));
        ad.setViewCount(null);

        ad.setTitle(request.getAd().getTitle());
        ad.setDescription(request.getAd().getDescription());
        ad.setImportantInfo(request.getAd().getImportantInfo());

        ad.setIsArchived(false);

        Ad savedAd = adRepository.save(ad);
        ServiceTypeCode serviceTypeCode = savedAd.getServiceType().getServiceTypeCode();

        if (serviceTypeCode == ServiceTypeCode.FREELANCE) {
            saveFreelancePackages(savedAd, request);
        } else {
            saveLocalServiceAndConsultation(savedAd, request);
            saveServiceTimeSlots(savedAd.getAdId(), userProfileId, request.getServiceTimeSlots());
        }

        saveAdImages(savedAd, request.getImages());

        String notificationText = "Успешно публикуване на нова обява в подкатегория " + subcategory.getSubcategoryName();
        notificationService.createNotification(
                userProfileRepository.findUserIdByUserProfileId(userProfileId),
                NotificationTypeName.POST_AD,
                EntityTypeName.AD,
                savedAd.getAdId(),
                notificationText
        );

        return savedAd.getAdId();
    }

    private LocalDate calculateExpiration(AdPricingPlan adPricingPlan) {
        PricingPlanCode code = adPricingPlan.getPlanCode();
        LocalDate today = LocalDate.now();
        return switch (code) {
            case DAYS_14 -> today.plusDays(14);
            case MONTH_1 -> today.plusMonths(1);
            case MONTHS_2 -> today.plusMonths(2);
            case MONTHS_3 -> today.plusMonths(3);
        };
    }
    private void saveServiceTimeSlots(Long adId, Long userProfileId, List<CalendarTimeSlotPayload> serviceTimeSlots) {
        if (serviceTimeSlots == null || serviceTimeSlots.isEmpty()) {
            return;
        }

        UserProfile userProfile = userProfileRepository.findById(userProfileId).orElseThrow();

        serviceTimeSlots.stream()
                .map(payload -> {
                    TimeSlotStatus timeSlotStatus = timeSlotStatusRepository
                            .findByTimeSlotStatusCode(payload.getTimeSlotStatusCode())
                            .orElseThrow();

                    ServiceTimeSlot serviceTimeSlot = new ServiceTimeSlot();
                    serviceTimeSlot.setUserProfile(userProfile);
                    serviceTimeSlot.setTimeSlotStatus(timeSlotStatus);
                    serviceTimeSlot.setDate(LocalDate.parse(payload.getDate()));
                    serviceTimeSlot.setStartTimeLabel(payload.getStartTimeLabel());
                    serviceTimeSlot.setEndTimeLabel(null);

                    ServiceTimeSlot savedServiceTimeSlot = serviceTimeSlotRepository.save(serviceTimeSlot);

                    serviceSystemTimeSlotRepository.save(
                            new ServiceSystemTimeSlot(payload.getSystemTimeSlotId(), savedServiceTimeSlot.getServiceTimeSlotId())
                    );

                    adServiceTimeSlotRepository.save(
                            new AdServiceTimeSlot(adId, savedServiceTimeSlot.getServiceTimeSlotId())
                    );

                    return savedServiceTimeSlot.getServiceTimeSlotId();
                })
                .toList();
    }
    private void saveLocalServiceAndConsultation(Ad savedAd, PublishAdRequest request) {
        LocalServicePayload localServicePayload = request.getLocalService();
        ConsultationPayload consultationPayload = request.getConsultation();

        if (localServicePayload == null) {
            return;
        }

        LocalService localService = new LocalService();
        localService.setAd(savedAd);
        localService.setServicePriceMin(localServicePayload.getServicePriceMin());
        localService.setServicePriceMax(localServicePayload.getServicePriceMax());
        localService.setProviderQuestion(localServicePayload.getProviderQuestion());
        localService.setImportantInfo(localServicePayload.getImportantInfo());

        LocalService savedLocalService = localServiceRepository.save(localService);

        if (consultationPayload == null) {
            return;
        }

        Consultation consultation = new Consultation();
        consultation.setLocalService(savedLocalService);
        consultation.setConsultationPriceMin(consultationPayload.getConsultationPriceMin());
        consultation.setConsultationPriceMax(consultationPayload.getConsultationPriceMax());

        consultationRepository.save(consultation);
    }
    private void saveFreelancePackages(Ad savedAd, PublishAdRequest request) {
        FreelanceResponseDto freelance = request.getFreelancePackage();
        if (freelance == null) {
            return;
        }

        saveOnePackage(savedAd, freelance.getBasic(), PackageTypeCode.BASIC);
        saveOnePackage(savedAd, freelance.getStandard(), PackageTypeCode.STANDARD);
        saveOnePackage(savedAd, freelance.getPremium(), PackageTypeCode.PREMIUM);
    }

    private void saveOnePackage(Ad savedAd, FreelancePackageDto dto, PackageTypeCode typeCode) {
        if (dto == null) {
            return;
        }

        PackageType packageType =
                packageTypeRepository.findByPackageTypeCode(typeCode).orElseThrow();

        FreelancePackage entity = new FreelancePackage();
        entity.setAd(savedAd);
        entity.setPackageType(packageType);
        entity.setPackageTitle(dto.getPackageTitle());
        entity.setPackageDescription(dto.getPackageDescription());
        entity.setPackagePrice(dto.getPackagePrice());
        entity.setDeliveryDays(dto.getDeliveryDays());
        entity.setRevisionCount(dto.getRevisionCount() != null ? dto.getRevisionCount().toString() : null);
        entity.setRenderCount(dto.getRenderCount());
        entity.setDetailDrawingCount(dto.getDetailDrawingCount());
        entity.setHas3dModel(dto.getHas3DModel());
        entity.setHas2dDrawings(dto.getHas2DDrawings());
        entity.setHasSourceFile(dto.getHasSourceFile());
        entity.setImportantInfo(dto.getPackageInfo());

        freelancePackageRepository.save(entity);
    }
    private void saveAdImages(Ad savedAd, List<ImagePayload> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        images.stream()
                .map(imagePayload -> {
                    CloudinaryService.UploadResult uploadResult =
                            cloudinaryService.uploadImage(imagePayload.getFile());

                    AdImage adImage = new AdImage();
                    adImage.setAd(savedAd);
                    adImage.setAdImageUrl(uploadResult.url());
                    adImage.setAdImageUrlId(uploadResult.publicId());
                    adImage.setIsPrimary(Boolean.TRUE.equals(imagePayload.getIsPrimary()));
                    adImage.setCreatedAt(java.time.LocalDateTime.now());

                   AdImage savedAdImage = adImageRepository.save(adImage);
                    return savedAdImage.getAdImageId();
                })
                .toList();
    }


    @Override
    public Long createServiceRequest(ServiceRequestPayload payload, Long userId) {
        Category category = categoryRepository.findById(payload.getCategoryId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        ClientServiceRequest request = new ClientServiceRequest();
        request.setCategory(category);
        request.setUser(user);
        request.setRequestTitle(payload.getTitle());
        request.setRequestDescription(payload.getDescription());

        return serviceRequestRepository.save(request).getRequestId();
    }
}

