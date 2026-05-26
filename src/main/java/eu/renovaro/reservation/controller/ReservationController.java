package eu.renovaro.reservation.controller;

import eu.renovaro.reservation.domain.CalendarDayDto;
import eu.renovaro.reservation.domain.ConsultationReservationDto;
import eu.renovaro.reservation.domain.ServiceReservationRequest;
import eu.renovaro.reservation.service.ReservationService;
import eu.renovaro.user.service.UserProfileService;
import eu.renovaro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final UserProfileService userProfileService;
    private final UserService userService;
    private final ReservationService reservationService;


    @PostMapping("/create-service-reservation")
    public ResponseEntity<Void> createServiceReservation(@RequestBody ServiceReservationRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        reservationService.createServiceReservation(userService.getUserInfoByEmail(auth.getName()).getUserId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/provider/consultations")
    public List<ConsultationReservationDto> getProviderConsultations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userProfileId = userProfileService.getUserProfileIdByEmail(auth.getName());

        return reservationService.getProviderConsultations(userProfileId);
    }

    @GetMapping("/provider-calendar")
    public List<CalendarDayDto> getProviderCalendar(@RequestParam LocalDate adExpirationDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userProfileId = userProfileService.getUserProfileIdByEmail(auth.getName());

        return reservationService.getProviderCalendar(userProfileId, adExpirationDate);
    }

    @PostMapping("/{reservationId}/confirm")
    public ResponseEntity<Void> confirmReservation(@PathVariable Long reservationId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userProfileId = userProfileService.getUserProfileIdByEmail(auth.getName());

        reservationService.confirmReservation(userProfileId, reservationId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reservationId}/decline")
    public ResponseEntity<Void> declineReservation(@PathVariable Long reservationId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userProfileId = userProfileService.getUserProfileIdByEmail(auth.getName());

        reservationService.declineReservation(userProfileId, reservationId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{reservationId}/toggle-is-archived")
    public ResponseEntity<Void> toggleIsArchived(@PathVariable Long reservationId,
                                              @RequestBody Map<String, Boolean> request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userProfileId = userProfileService.getUserProfileIdByEmail(auth.getName());

        Boolean isArchived = request.get("isArchived");
        reservationService.toggleIsArchived(userProfileId , reservationId, isArchived);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByEmail(auth.getName());

        reservationService.cancelReservation(userId, reservationId);

        return ResponseEntity.ok().build();
    }

}
