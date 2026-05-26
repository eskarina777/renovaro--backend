package eu.renovaro.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "established_on")
    private LocalDate establishedOn;

    @Column(name = "number_employees")
    private Integer numberEmployees;

    @Column(name = "field_of_operation")
    private String fieldOfOperation;

    @OneToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}
