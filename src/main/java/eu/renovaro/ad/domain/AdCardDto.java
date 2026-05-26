package eu.renovaro.ad.domain;

import eu.renovaro.user.domain.entity.UserRole;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdCardDto {

    private Long adId;
    private String adStatus;
    private Long userId;
    private String userName;
    private String profileImageUrl;
    private ServiceTypeCode serviceType;
    private String adTitle;
    private Double ratingAverage;
    private Integer ratingCount;
    private BigDecimal adPriceMin;
    private BigDecimal adPriceMax;
    private String pricingUnit;
    private String adImageUrl;
    private ProviderRole userRole;
    private String city;
}
