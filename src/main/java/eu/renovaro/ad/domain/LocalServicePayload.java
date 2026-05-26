package eu.renovaro.ad.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalServicePayload {
    private BigDecimal servicePriceMin;
    private BigDecimal servicePriceMax;
    private String providerQuestion;
    private String importantInfo;
}
