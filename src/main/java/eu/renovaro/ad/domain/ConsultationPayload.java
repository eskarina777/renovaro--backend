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
public class ConsultationPayload {
    private BigDecimal consultationPriceMin;
    private BigDecimal consultationPriceMax;
}
