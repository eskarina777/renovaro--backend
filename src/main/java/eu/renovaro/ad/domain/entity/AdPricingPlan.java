package eu.renovaro.ad.domain.entity;

import eu.renovaro.ad.domain.PricingPlanCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_pricing_plan")
public class AdPricingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_pricing_plan_id")
    private Long adPricingPlanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_code")
    private PricingPlanCode planCode;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "credits")
    private Integer credits;

    @OneToMany(mappedBy = "adPricingPlan")
    private List<Ad> ads;
}
