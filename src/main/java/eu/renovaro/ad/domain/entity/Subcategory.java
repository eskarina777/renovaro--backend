package eu.renovaro.ad.domain.entity;

import eu.renovaro.user.domain.entity.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subcategory")
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subcategory_id")
    private Long subcategoryId;

    @Column(name = "subcategory_code")
    private String subcategoryCode;

    @Column(name = "subcategory_name")
    private String subcategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @OneToMany(mappedBy = "subcategory")
    private List<Ad> adList;
}

