package eu.renovaro.ad.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdPricingPlanDto {
    private Long adPricingPlanId;
    private String planCode;
    private String planName;
    private Integer credits;
}