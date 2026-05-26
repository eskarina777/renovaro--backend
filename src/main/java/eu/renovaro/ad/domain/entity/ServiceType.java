package eu.renovaro.ad.domain.entity;

import eu.renovaro.ad.domain.ServiceTypeCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_type")
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_type_id")
    private Long serviceTypeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type_code")
    private ServiceTypeCode serviceTypeCode;

    @Column(name = "service_type_label")
    private String serviceTypeLabel;

    @OneToMany(mappedBy = "serviceType")
    private List<Ad> ads;
}

