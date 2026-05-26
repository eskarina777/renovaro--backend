package eu.renovaro.auth.domain;

import eu.renovaro.user.domain.RoleName;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private String phoneNumber;
    private String email;
    private String password;
    private RoleName role;
}
