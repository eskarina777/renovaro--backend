package eu.renovaro.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistPayload {
    private String firstName;
    private String lastName;
    private String description;
    private String phone;
    private Boolean publishPhone;
    private String website;
}