package eu.renovaro.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Long userId;
    private String userName;
    private List<RoleName> userRole;
    private String userProfileImage;
}
