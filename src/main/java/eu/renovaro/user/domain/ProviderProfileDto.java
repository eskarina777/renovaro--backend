package eu.renovaro.user.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderProfileDto {
    private Double ratingAverage;
    private Integer ratingCount;
    private String userWebsite;
    private String memberSince;
    private String phoneNumber;
    private Boolean showPhoneNumber;
    private Integer totalCredit;
}
