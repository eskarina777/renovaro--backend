package eu.renovaro.ad.domain;

import eu.renovaro.reservation.domain.CalendarTimeSlotPayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublishAdRequest {
    private AdPayload ad;
    private List<CalendarTimeSlotPayload> serviceTimeSlots;
    private LocalServicePayload localService;
    private ConsultationPayload consultation;
    private FreelanceResponseDto freelancePackage;
    private List<ImagePayload> images;
}
