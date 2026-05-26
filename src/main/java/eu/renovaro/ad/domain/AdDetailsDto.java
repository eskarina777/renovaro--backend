package eu.renovaro.ad.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdDetailsDto {
    private String city;
    private String adTitle;
    private String adDescription;
    private BigDecimal adPriceMin;
    private BigDecimal adPriceMax;
    private String pricingUnit;
    private List<AdImageUrlDto> adImageUrl;
    private String importantInfo;
}
