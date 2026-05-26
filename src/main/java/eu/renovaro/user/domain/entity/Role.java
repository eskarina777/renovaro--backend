package eu.renovaro.user.domain.entity;

import eu.renovaro.user.domain.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "role_label")
    private String roleLabel;

    @ToString.Exclude
    @OneToMany(mappedBy = "role")
    private Set<UserRole> users;

}
