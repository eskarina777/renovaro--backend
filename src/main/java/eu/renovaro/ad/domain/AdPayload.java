package eu.renovaro.ad.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdPayload {
    private Long cityId;
    private Long adPricingPlanId;
    private ServiceTypeCode serviceTypeCode;
    private Long pricingUnitId;
    private Long subcategoryId;
    private String title;
    private String description;
    private String importantInfo;
}
