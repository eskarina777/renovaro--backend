package eu.renovaro.user.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long userId;
    private String userName;
    private List<RoleName> userRole;
    private String userProfileImageUrl;
    private String userWebsite;
    private String userDetails;
    private Double ratingAverage;
    private Integer ratingCount;
    private String memberSince;
    private String phoneNumber;
    private Boolean showPhoneNumber;
}
