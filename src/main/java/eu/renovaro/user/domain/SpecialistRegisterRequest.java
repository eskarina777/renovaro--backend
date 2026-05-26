package eu.renovaro.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistRegisterRequest {
    private String email;
    private String password;
    private SpecialistPayload specialistProfile;
}