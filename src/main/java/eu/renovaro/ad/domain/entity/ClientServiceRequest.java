package eu.renovaro.ad.domain.entity;

import eu.renovaro.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_service_request")
public class ClientServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "request_title")
    private String requestTitle;

    @Column(name = "request_description")
    private String requestDescription;
}
