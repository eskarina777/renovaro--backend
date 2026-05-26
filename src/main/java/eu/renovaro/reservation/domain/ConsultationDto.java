package eu.renovaro.reservation.domain;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDto {
    private Long consultationId;
    private BigDecimal consultationPriceMin;
    private BigDecimal consultationPriceMax;
}
