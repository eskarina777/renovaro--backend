package eu.renovaro.ad.domain.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "local_service")
public class LocalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "local_service_id")
    private Long localServiceId;

    @OneToOne
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    @Column(name = "service_price_min")
    private BigDecimal servicePriceMin;

    @Column(name = "service_price_max")
    private BigDecimal servicePriceMax;

    @Column(name = "provider_question")
    private String providerQuestion;

    @Column(name = "important_info")
    private String importantInfo;

    @OneToOne(mappedBy = "localService")
    private Consultation consultation;
}
