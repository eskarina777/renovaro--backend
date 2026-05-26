package eu.renovaro.ad.domain.entity;

import eu.renovaro.ad.domain.PricingUnitCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pricing_unit")
public class PricingUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pricing_unit_id")
    private Long pricingUnitId;

    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_unit_code")
    private PricingUnitCode pricingUnitCode;

    @Column(name = "pricing_unit_Label")
    private String pricingUnitLabel;

    @OneToMany(mappedBy = "pricingUnit")
    private List<Ad> adList;
}
