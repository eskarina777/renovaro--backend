package eu.renovaro.reservation.service.impl;


import eu.renovaro.ad.domain.entity.Ad;
import eu.renovaro.ad.domain.entity.Consultation;
import eu.renovaro.ad.domain.entity.LocalService;
import eu.renovaro.ad.repository.AdRepository;
import eu.renovaro.ad.repository.ConsultationRepository;
import eu.renovaro.ad.repository.LocalServiceRepository;
import eu.renovaro.common.util.DateUtil;
import eu.renovaro.common.util.UserNameUtil;
import eu.renovaro.notification.domain.EntityTypeName;
import eu.renovaro.notification.domain.NotificationTypeName;
import eu.renovaro.notification.service.NotificationService;
import eu.renovaro.reservation.domain.*;
import eu.renovaro.reservation.domain.entity.*;
import eu.renovaro.reservation.repository.*;
import eu.renovaro.reservation.service.ReservationService;
import eu.renovaro.user.domain.AddressDto;
import eu.renovaro.user.domain.AddressRequest;
import eu.renovaro.user.domain.entity.Address;
import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.repository.AddressRepository;
import eu.renovaro.user.repository.CityRepository;
import eu.renovaro.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static eu.renovaro.common.util.UserNameUtil.constructUserName;
import static eu.renovaro.common.util.DateUtil.formatDate;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ServiceReservationRepository serviceReservationRepository;
    private final LocalServiceRepository localServiceRepository;
    private final ConsultationRepository consultationRepository;
    private final AdServiceTimeSlotRepository adServiceTimeSlotRepository;
    private final AdRepository adRepository;
    private final SystemTimeSlotRepository systemTimeSlotRepository;
    private final ServiceTimeSlotRepository serviceTimeSlotRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ReStatusRepository reStatusRepository;
    private final CityRepository cityRepository;
    private final TimeSlotStatusRepository timeSlotStatusRepository;
    private final NotificationService notificationService;

    public ReservationServiceImpl(
            AdServiceTimeSlotRepository adServiceTimeSlotRepository,
            LocalServiceRepository localServiceRepository,
            ConsultationRepository consultationRepository,
            AdRepository adRepository,
            ServiceReservationRepository serviceReservationRepository,
            SystemTimeSlotRepository systemTimeSlotRepository,
            ServiceTimeSlotRepository serviceTimeSlotRepository,
            AddressRepository addressRepository,
            UserRepository userRepository,
            ReStatusRepository reStatusRepository,
            CityRepository cityRepository,
            TimeSlotStatusRepository timeSlotStatusRepository,
            NotificationService notificationService

    ) {
        this.localServiceRepository = localServiceRepository;
        this.consultationRepository = consultationRepository;
        this.adServiceTimeSlotRepository = adServiceTimeSlotRepository;
        this.adRepository = adRepository;
        this.serviceReservationRepository = serviceReservationRepository;
        this.systemTimeSlotRepository = systemTimeSlotRepository;
        this.serviceTimeSlotRepository = serviceTimeSlotRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.reStatusRepository = reStatusRepository;
        this.cityRepository = cityRepository;
        this.timeSlotStatusRepository = timeSlotStatusRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void cancelReservation(Long userId, Long reservationId) {
        ServiceReservation reservation = serviceReservationRepository.findById(reservationId)
                .orElseThrow();

        User client = reservation.getUser();
        ServiceTimeSlot slot = reservation.getServiceTimeSlot();
        User provider = slot != null && slot.getUserProfile() != null
                ? slot.getUserProfile().getUser()
                : null;

        boolean isClient = client != null && client.getUserId().equals(userId);
        boolean isProvider = provider != null && provider.getUserId().equals(userId);

        if (!isClient && !isProvider) {
            throw new IllegalStateException();
        }

        ReStatus cancelledStatus = reStatusRepository.findByReStatusCode(ReStatusCode.CANCELLED)
                .orElseThrow();
        reservation.setReStatus(cancelledStatus);
        ServiceReservation savedReservation = serviceReservationRepository.save(reservation);

        TimeSlotStatus availableStatus = timeSlotStatusRepository
                .findByTimeSlotStatusCode(TimeSlotStatusCode.AVAILABLE)
                .orElseThrow();
        slot.setTimeSlotStatus(availableStatus);
        serviceTimeSlotRepository.save(slot);

        String dateLabel = DateUtil.formatDate(slot.getDate());
        String timeLabel = slot.getStartTimeLabel();

        Long recipientUserId;
        String text;

        if (isClient) {
            recipientUserId = provider.getUserId();
            text = constructUserName(provider) + " е отменил/а резервацията Ви за " + dateLabel + " от " + timeLabel + " часа";
        } else {
            recipientUserId = client.getUserId();
            text = constructUserName(client) + " е отменил/а резервацията си за " + dateLabel + " от " + timeLabel + " часа";
        }

        notificationService.createNotification(
                recipientUserId,
                NotificationTypeName.RESERVATION_CANCELLED,
                EntityTypeName.SERVICE_RESERVATION,
                savedReservation.getServiceReservationId(),
                text
        );
    }

    @Override
    public void confirmReservation(Long providerId, Long serviceReservationId) {
        ServiceReservation reservation = serviceReservationRepository.findById(serviceReservationId).orElseThrow();

        ServiceTimeSlot slot = reservation.getServiceTimeSlot();
        if (slot == null || slot.getUserProfile() == null || !slot.getUserProfile().getUserProfileId().equals(providerId)) {
            throw new IllegalStateException("Reservation does not belong to this provider");
        }

        ReStatus confirmedStatus = reStatusRepository.findByReStatusCode(ReStatusCode.CONFIRMED).orElseThrow();
        reservation.setReStatus(confirmedStatus);

        ServiceReservation savedReservation = serviceReservationRepository.save(reservation);

        Long clientId = savedReservation.getUser() != null ? savedReservation.getUser().getUserId() : null;
        if (clientId != null) {
            String providerName = UserNameUtil.constructUserName(slot.getUserProfile().getUser());
            String dateLabel = DateUtil.formatDate(slot.getDate());
            String timeLabel = slot.getStartTimeLabel();

            String text = "Вашата резервация при "+ providerName + " за " + dateLabel + " от " + timeLabel + " e потвърдена.";

            notificationService.createNotification(
                    clientId,
                    NotificationTypeName.RESERVATION_CONFIRMED,
                    EntityTypeName.SERVICE_RESERVATION,
                    savedReservation.getServiceReservationId(),
                    text
            );
        }
    }

    @Override
    public void declineReservation(Long providerId, Long serviceReservationId) {
        ServiceReservation reservation = serviceReservationRepository.findById(serviceReservationId).orElseThrow();

        ServiceTimeSlot slot = reservation.getServiceTimeSlot();
        if (slot == null || slot.getUserProfile() == null || !slot.getUserProfile().getUserProfileId().equals(providerId)) {
            throw new IllegalStateException("Reservation does not belong to this provider");
        }

        ReStatus declinedStatus = reStatusRepository.findByReStatusCode(ReStatusCode.DECLINED).orElseThrow();
        reservation.setReStatus(declinedStatus);

        ServiceReservation savedReservation = serviceReservationRepository.save(reservation);

        TimeSlotStatus availableStatus = timeSlotStatusRepository.findByTimeSlotStatusCode(TimeSlotStatusCode.AVAILABLE).orElseThrow();
        slot.setTimeSlotStatus(availableStatus);
        serviceTimeSlotRepository.save(slot);

        Long clientId = savedReservation.getUser() != null ? savedReservation.getUser().getUserId() : null;
        if (clientId != null) {
            String providerName = UserNameUtil.constructUserName(slot.getUserProfile().getUser());
            String dateLabel = DateUtil.formatDate(slot.getDate());
            String timeLabel = slot.getStartTimeLabel();

            String text = "Вашата резервация при " + providerName +" за " + dateLabel + " от " + timeLabel + " e отказана.";

            notificationService.createNotification(
                    clientId,
                    NotificationTypeName.RESERVATION_DECLINED,
                    EntityTypeName.SERVICE_RESERVATION,
                    savedReservation.getServiceReservationId(),
                    text
            );
        }
    }

    @Override
    public void toggleIsArchived(Long userProfileId, Long reservationId, Boolean isArchived) {
        if (isArchived == null) {
            throw new IllegalArgumentException();
        }

        ServiceReservation reservation = serviceReservationRepository.findById(reservationId)
                .orElseThrow();

        ServiceTimeSlot slot = reservation.getServiceTimeSlot();
        if (slot == null || slot.getUserProfile() == null ||
                !slot.getUserProfile().getUserProfileId().equals(userProfileId)) {
            throw new IllegalStateException();
        }

        reservation.setIsArchived(isArchived);
        serviceReservationRepository.save(reservation);
    }




    @Override
    public void createServiceReservation(Long userId, ServiceReservationRequest request) {
        User user = userRepository.findById(userId).orElseThrow();

        ServiceTimeSlot slot = serviceTimeSlotRepository.findById(request.getServiceTimeSlotId()).orElseThrow();

        if (slot.getTimeSlotStatus() == null || slot.getTimeSlotStatus().getTimeSlotStatusCode() != TimeSlotStatusCode.AVAILABLE) {
            throw new IllegalStateException("Time slot is not available");
        }

        Address address;
        if (request.getAddressId() != null) {
            address = addressRepository.findById(request.getAddressId()).orElseThrow();
            if (!address.getUser().getUserId().equals(userId)) {
                throw new IllegalStateException();
            }
        } else {
            AddressRequest addressRequest = request.getAddress();
            if (addressRequest == null) {
                throw new IllegalStateException();
            }

            address = new Address();
            address.setUser(user);
            address.setStreet(addressRequest.getStreet());
            address.setStreetNumber(addressRequest.getStreetNumber());
            address.setDistrict(addressRequest.getDistrict());
            address.setCity(cityRepository.findById(addressRequest.getCityId()).orElseThrow());
            address = addressRepository.save(address);
        }

        if (user.getPhoneNumber() == null) {
            if (request.getPhoneNumber() == null) {
                throw new IllegalStateException();
            }
            user.setPhoneNumber(request.getPhoneNumber());
            userRepository.save(user);
        }

        ReStatus reStatus = reStatusRepository.findByReStatusCode(ReStatusCode.PENDING).orElseThrow();

        ServiceReservation reservation = new ServiceReservation();
        reservation.setUser(user);
        reservation.setServiceTimeSlot(slot);
        reservation.setAddress(address);
        reservation.setReStatus(reStatus);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setIsArchived(false);
        ServiceReservation savedReservation = serviceReservationRepository.save(reservation);

        Long providerId = slot.getUserProfile().getUser().getUserId();
        String clientName = constructUserName(savedReservation.getUser());
        String dateLabel = formatDate(slot.getDate());
        String timeLabel = slot.getStartTimeLabel();
        String notificationText = "Заявка за резервация от " + clientName + " за " + dateLabel + " от " + timeLabel + " часа";

        TimeSlotStatus reserved = timeSlotStatusRepository
                .findByTimeSlotStatusCode(TimeSlotStatusCode.RESERVED)
                .orElseThrow();
        slot.setTimeSlotStatus(reserved);
        serviceTimeSlotRepository.save(slot);

        notificationService.createNotification(
                providerId,
                NotificationTypeName.RESERVATION_CREATED,
                EntityTypeName.SERVICE_RESERVATION,
                savedReservation.getServiceReservationId(),
                notificationText
        );

    }

    @Override
    public List<CalendarDayDto> getProviderCalendar(Long userProfileId, LocalDate adExpirationDate) {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = adExpirationDate;

        if (endDate == null || endDate.isBefore(startDate)) {
            return List.of();
        }

        List<SystemTimeSlot> systemTimeSlots = systemTimeSlotRepository.findAllOrdered();
        List<ServiceTimeSlot> providerSlots = serviceTimeSlotRepository.findProviderSlotsBetween(userProfileId, startDate, endDate);

        Map<LocalDate, Map<Long, ServiceTimeSlot>> byDate = new HashMap<>();

        for (ServiceTimeSlot serviceTimeSlot : providerSlots) {
            if (serviceTimeSlot.getDate() == null) continue;
            if (serviceTimeSlot.getServiceSystemTimeSlotList() == null) continue;

            Map<Long, ServiceTimeSlot> dayMap = byDate.computeIfAbsent(serviceTimeSlot.getDate(), d -> new HashMap<>());

            for (ServiceSystemTimeSlot slot : serviceTimeSlot.getServiceSystemTimeSlotList()) {
                if (slot.getSystemTimeSlotId() == null) continue;
                dayMap.put(slot.getSystemTimeSlotId(), serviceTimeSlot);
            }
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        return java.util.stream.Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(Math.max(days, 0))
                .map(date -> {
                    Map<Long, ServiceTimeSlot> dayMap = byDate.getOrDefault(date, Collections.emptyMap());

                    List<TimeSlotDto> timeSlots = systemTimeSlots.stream()
                            .map(systemSlot -> {
                                ServiceTimeSlot existing = dayMap.get(systemSlot.getSystemTimeSlotId());

                                TimeSlotDto dto = new TimeSlotDto();
                                dto.setSystemTimeSlotId(systemSlot.getSystemTimeSlotId());
                                dto.setStartTimeLabel(systemSlot.getTimeSlotLabel());

                                if (existing == null) {
                                    dto.setServiceTimeSlotId(null);
                                    dto.setTimeSlotStatusCode(null);
                                } else {
                                    dto.setServiceTimeSlotId(existing.getServiceTimeSlotId());
                                    dto.setTimeSlotStatusCode(existing.getTimeSlotStatus() != null
                                            ? existing.getTimeSlotStatus().getTimeSlotStatusCode()
                                            : null);
                                }

                                return dto;
                            })
                            .toList();

                    CalendarDayDto dayDto = new CalendarDayDto();
                    dayDto.setDate(date.toString());
                    dayDto.setTimeSlots(timeSlots);
                    return dayDto;
                })
                .toList();
    }
        @Override
        public List<ConsultationReservationDto> getProviderConsultations(Long userProfileId) {
            return serviceReservationRepository.findProviderConsultations(userProfileId).stream()
                    .sorted(Comparator
                            .comparing(ServiceReservation::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(ServiceReservation::getServiceReservationId, Comparator.nullsLast(Comparator.reverseOrder()))
                    )
                    .map(this::toDto)
                    .toList();
        }

        private ConsultationReservationDto toDto(ServiceReservation sr) {
            ConsultationReservationDto dto = new ConsultationReservationDto();

            dto.setReStatus(sr.getReStatus() != null ? sr.getReStatus().getReStatusCode() : null);

            AddressDto address = new AddressDto();
            address.setCity(sr.getAddress() != null ? sr.getAddress().getCity().getCityName() : null);
            address.setDistrict(sr.getAddress() != null ? sr.getAddress().getDistrict() : null);
            address.setStreet(sr.getAddress() != null ? sr.getAddress().getStreet() : null);
            address.setStreetNumber(sr.getAddress() != null ? sr.getAddress().getStreetNumber() : null);
            dto.setAddress(address);

            UserContactInfoDto contact = new UserContactInfoDto();
            User client = sr.getUser();
            contact.setUserName(constructUserName(client));
            contact.setUserPhoneNumber(client != null ? client.getPhoneNumber() : null);
            dto.setUserContactInfo(contact);

            ConsultationDto consultation = new ConsultationDto();
            if (sr.getServiceTimeSlot() != null
                    && sr.getServiceTimeSlot().getAdServiceTimeSlotList() != null
                    && !sr.getServiceTimeSlot().getAdServiceTimeSlotList().isEmpty()
                    && sr.getServiceTimeSlot().getAdServiceTimeSlotList().get(0).getAd() != null
                    && sr.getServiceTimeSlot().getAdServiceTimeSlotList().get(0).getAd().getLocalService() != null
                    && sr.getServiceTimeSlot().getAdServiceTimeSlotList().get(0).getAd().getLocalService().getConsultation() != null
            ) {
                var c = sr.getServiceTimeSlot().getAdServiceTimeSlotList().get(0).getAd().getLocalService().getConsultation();
                consultation.setConsultationId(c.getConsultationId());
                consultation.setConsultationPriceMin(c.getConsultationPriceMin());
                consultation.setConsultationPriceMax(c.getConsultationPriceMax());
            } else {
                consultation.setConsultationId(null);
                consultation.setConsultationPriceMin(null);
                consultation.setConsultationPriceMax(null);
            }
            dto.setConsultationPrice(consultation);

            AppointmentDto appointment = new AppointmentDto();
            appointment.setServiceBookingId(sr.getServiceReservationId());
            appointment.setDate(sr.getServiceTimeSlot() != null && sr.getServiceTimeSlot().getDate() != null
                    ? sr.getServiceTimeSlot().getDate().toString()
                    : null);
            appointment.setStartTimeLabel(sr.getServiceTimeSlot() != null ? sr.getServiceTimeSlot().getStartTimeLabel() : null);
            appointment.setEndTimeLabel(null);
            appointment.setIsArchived(Boolean.TRUE.equals(sr.getIsArchived()));

            dto.setAppointment(appointment);

            return dto;
        }

    @Override
    public ConsultationResponseDto getConsultationPanel(Long adId) {

        LocalDate start = LocalDate.now().plusDays(1);

        Ad ad = adRepository.findById(adId).orElseThrow();
        LocalDate end = ad.getAdExpirationDate();

        LocalService localService = localServiceRepository.findByAd_AdId(adId);
        String importantInfo = (localService != null) ? localService.getImportantInfo() : null;

        ConsultationDto consultationDto = null;
        if (localService != null) {
            Consultation consultation = consultationRepository
                    .findByLocalService_LocalServiceId(localService.getLocalServiceId())
                    .orElse(null);

            if (consultation != null) {
                consultationDto = new ConsultationDto(
                        consultation.getConsultationId(),
                        consultation.getConsultationPriceMin(),
                        consultation.getConsultationPriceMax()
                );
            }
        }

        List<ServiceTimeSlot> slots = adServiceTimeSlotRepository.findSlotsByAdId(adId);

        Map<LocalDate, List<TimeSlotDto>> map = slots.stream()
                .filter(sts -> sts.getDate() != null)
                .filter(sts -> !sts.getDate().isBefore(start) && !sts.getDate().isAfter(end))
                .collect(java.util.stream.Collectors.groupingBy(
                        ServiceTimeSlot::getDate,
                        java.util.HashMap::new,
                        java.util.stream.Collectors.mapping(sts -> {
                            TimeSlotDto dto = new TimeSlotDto();
                            dto.setServiceTimeSlotId(sts.getServiceTimeSlotId());
                            dto.setStartTimeLabel(sts.getStartTimeLabel());
                            dto.setTimeSlotStatusCode(
                                    sts.getTimeSlotStatus() != null ? sts.getTimeSlotStatus().getTimeSlotStatusCode() : null
                            );

                            Long systemTimeSlotId = (sts.getServiceSystemTimeSlotList() == null)
                                    ? null
                                    : sts.getServiceSystemTimeSlotList().stream()
                                    .map(ServiceSystemTimeSlot::getSystemTimeSlotId)
                                    .filter(java.util.Objects::nonNull)
                                    .min(Long::compareTo)
                                    .orElse(null);

                            dto.setSystemTimeSlotId(systemTimeSlotId);
                            return dto;
                        }, java.util.stream.Collectors.toList())
                ));

        long days = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;

        List<CalendarDayDto> calendar = java.util.stream.Stream.iterate(start, d -> d.plusDays(1))
                .limit(Math.max(days, 0))
                .map(d -> {
                    List<TimeSlotDto> daySlots = map.get(d);
                    return new CalendarDayDto(d.toString(), (daySlots == null || daySlots.isEmpty()) ? null : daySlots);
                })
                .toList();

        return new ConsultationResponseDto(consultationDto, calendar, importantInfo);
    }

}
