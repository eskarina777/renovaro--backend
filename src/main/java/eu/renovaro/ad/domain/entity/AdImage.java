package eu.renovaro.ad.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_image")
public class AdImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_image_id")
    private Long adImageId;

    @Column(name = "ad_image_url")
    private String adImageUrl;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "ad_image_url_id")
    private String adImageUrlId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;
}
