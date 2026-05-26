package eu.renovaro.user.domain.entity;

import eu.renovaro.ad.domain.entity.Ad;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "city_name")
    private String cityName;

    @OneToMany(mappedBy = "city")
    private List<Address> addresses;

    @OneToMany(mappedBy = "city")
    private List<Ad> ads;

}
