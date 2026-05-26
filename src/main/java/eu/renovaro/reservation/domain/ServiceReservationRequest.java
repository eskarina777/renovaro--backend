package eu.renovaro.reservation.domain;

import eu.renovaro.user.domain.AddressDto;
import eu.renovaro.user.domain.AddressRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReservationRequest {
    private Long serviceTimeSlotId;
    private Long addressId;
    private String phoneNumber;
    private AddressRequest address;
}
