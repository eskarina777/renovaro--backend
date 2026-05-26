package eu.renovaro.ad.domain.entity;

import eu.renovaro.ad.domain.PackageTypeCode;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "package_type")
public class PackageType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_type_id")
    private Long packageTypeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_type_code")
    private PackageTypeCode packageTypeCode;

    @Column(name = "package_type_label")
    private String packageTypeLabel;
}
