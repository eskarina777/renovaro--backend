package eu.renovaro.reservation.controller;


import eu.renovaro.reservation.domain.ConsultationResponseDto;
import eu.renovaro.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/reservation")
public class ReservationPublicController {

    private final ReservationService reservationService;

    public ReservationPublicController(ReservationService reservationPublicService) {
        this.reservationService = reservationPublicService;
    }

    @GetMapping("/consultation-panel/{adId}")
    public ConsultationResponseDto getConsultationPanel(@PathVariable Long adId) {
        return reservationService.getConsultationPanel(adId);
    }
}
