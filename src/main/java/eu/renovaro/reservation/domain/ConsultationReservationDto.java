package eu.renovaro.reservation.domain;

import eu.renovaro.user.domain.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationReservationDto {
    private ReStatusCode reStatus;
    private AddressDto address;
    private UserContactInfoDto userContactInfo;
    private ConsultationDto consultationPrice;
    private AppointmentDto appointment;
}
