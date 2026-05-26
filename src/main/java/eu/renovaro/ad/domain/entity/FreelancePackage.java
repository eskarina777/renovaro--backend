package eu.renovaro.ad.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "freelance_package")
public class FreelancePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long packageId;

    @ManyToOne
    @JoinColumn(name = "package_type_id", nullable = false)
    private PackageType packageType;

    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    @Column(name = "package_title")
    private String packageTitle;

    @Column(name = "package_description")
    private String packageDescription;

    @Column(name = "package_price")
    private BigDecimal packagePrice;

    @Column(name = "delivery_days", nullable = false)
    private Integer deliveryDays;

    @Column(name = "revision_count")
    private String revisionCount;

    @Column(name = "render_count")
    private Integer renderCount;

    @Column(name = "detail_drawing_count")
    private Integer detailDrawingCount;

    @Column(name = "has_source_file")
    private Boolean hasSourceFile;

    @Column(name = "has_3d_model")
    private Boolean has3dModel;

    @Column(name = "has_2d_drawings")
    private Boolean has2dDrawings;

    @Column(name = "important_info")
    private String importantInfo;
}