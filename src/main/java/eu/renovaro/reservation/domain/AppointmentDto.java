package eu.renovaro.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private Long serviceBookingId;
    private String date;
    private String startTimeLabel;
    private String endTimeLabel;
    private Boolean isArchived;
}
