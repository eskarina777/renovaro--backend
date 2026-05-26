package eu.renovaro.notification.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import eu.renovaro.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_object")
public class NotificationObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_object_id")
    private Long notificationObjectId;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "entity_type_id")
    private EntityType entityType;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    @OneToMany(mappedBy = "notificationObject")
    private List<Notification> notifications;

}
