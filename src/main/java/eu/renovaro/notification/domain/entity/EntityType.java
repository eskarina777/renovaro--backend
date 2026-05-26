package eu.renovaro.notification.domain.entity;

import eu.renovaro.notification.domain.EntityTypeName;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entity_type")
public class EntityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entity_type_id")
    private Long entityTypeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type_name")
    private EntityTypeName entityTypeName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "entityType")
    private List<NotificationObject> notificationObjects;

}
