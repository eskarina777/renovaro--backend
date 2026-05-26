package eu.renovaro.ad.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import eu.renovaro.ad.domain.entity.LocalService;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_id")
    private Long consultationId;

    @OneToOne
    @JoinColumn(name = "local_service_id")
    private LocalService localService;

    @Column(name = "consultation_price_min")
    private BigDecimal consultationPriceMin;

    @Column(name = "consultation_price_max")
    private BigDecimal consultationPriceMax;
}
