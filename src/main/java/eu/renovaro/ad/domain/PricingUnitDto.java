package eu.renovaro.ad.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PricingUnitDto {
    private Long pricingUnitId;
    private PricingUnitCode pricingUnitCode;
    private String pricingUnitLabel;
}