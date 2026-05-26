package eu.renovaro.reservation.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDto {
    private Long serviceTimeSlotId;
    private Long systemTimeSlotId;
    private String startTimeLabel;
    private TimeSlotStatusCode timeSlotStatusCode;
}
