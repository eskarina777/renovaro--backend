package eu.renovaro.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarTimeSlotPayload {
    private String date;
    private Long systemTimeSlotId;
    private Long serviceTimeSlotId;
    private String startTimeLabel;
    private TimeSlotStatusCode timeSlotStatusCode;
}
