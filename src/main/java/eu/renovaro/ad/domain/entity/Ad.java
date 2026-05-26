package eu.renovaro.ad.domain.entity;

import eu.renovaro.user.domain.entity.City;
import eu.renovaro.user.domain.entity.UserProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Long adId;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "ad_status_id")
    private AdStatus adStatus;

    @ManyToOne
    @JoinColumn(name = "ad_pricing_plan_id")
    private AdPricingPlan adPricingPlan;

    @ManyToOne
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

    @ManyToOne
    @JoinColumn(name = "pricing_unit_id")
    private PricingUnit pricingUnit;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "ad_expiration_date")
    private LocalDate adExpirationDate;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "ad_title")
    private String title;

    @Column(name = "ad_description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "important_info")
    private String importantInfo;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @OneToOne(mappedBy = "ad")
    private LocalService localService;

    @OneToMany(mappedBy = "ad")
    private List<FreelancePackage> freelancePackages;
}
