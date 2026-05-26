package eu.renovaro.reservation.domain;

import eu.renovaro.reservation.domain.TimeSlotDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDayDto {
    private String date;
    private List<TimeSlotDto> timeSlots;
}
