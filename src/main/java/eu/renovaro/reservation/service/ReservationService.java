package eu.renovaro.reservation.service;

import eu.renovaro.reservation.domain.CalendarDayDto;
import eu.renovaro.reservation.domain.ConsultationReservationDto;
import eu.renovaro.reservation.domain.ConsultationResponseDto;
import eu.renovaro.reservation.domain.ServiceReservationRequest;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    ConsultationResponseDto getConsultationPanel(Long adId);
    List<ConsultationReservationDto> getProviderConsultations(Long userProfileId);
    List<CalendarDayDto> getProviderCalendar(Long userProfileId, LocalDate adExpirationDate);
    void createServiceReservation(Long userId, ServiceReservationRequest request);
    void confirmReservation(Long providerUserProfileId, Long serviceReservationId);
    void declineReservation(Long providerUserProfileId, Long serviceReservationId);
    void toggleIsArchived(Long providerUserProfileId, Long reservationId, Boolean isArchived);
    void cancelReservation(Long userId, Long reservationId);
}
