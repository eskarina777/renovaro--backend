package eu.renovaro.reservation.domain.entity;

import eu.renovaro.reservation.domain.ReStatusCode;
import eu.renovaro.user.domain.entity.Address;
import eu.renovaro.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "re_status")
public class ReStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "re_status_id")
    private Long reStatusId;

    @Enumerated(EnumType.STRING)
    @Column(name = "re_status_code")
    private ReStatusCode reStatusCode;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "reStatus")
    private List<ServiceReservation> serviceReservationlist;

    @OneToMany(mappedBy = "reStatus")
    private List<FreelanceRequest> freelanceRequestList;

}