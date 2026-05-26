package eu.renovaro.ad.domain.entity;

import eu.renovaro.ad.domain.AdStatusCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_status")
public class AdStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_status_id")
    private Long adStatusId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ad_status_code")
    private AdStatusCode adStatusCode;

    @OneToMany(mappedBy = "adStatus")
    private List<Ad> ads;
}
