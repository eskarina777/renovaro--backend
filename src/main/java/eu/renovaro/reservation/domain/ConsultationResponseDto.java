package eu.renovaro.reservation.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResponseDto {
    private ConsultationDto consultation;
    private List<CalendarDayDto> adCalendar;
    private String importantInfo;
}
