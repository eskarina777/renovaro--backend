package eu.renovaro.notification.domain.entity;

import eu.renovaro.notification.domain.NotificationTypeName;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_type")
public class NotificationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_type_id")
    private Long notificationTypeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type_name")
    private NotificationTypeName notificationTypeName;

    @Column(name = "notification_type_code")
    private String notificationTypeCode;

    @Column(name = "notification_title")
    private String notificationTitle;

    @Column(name = "is_required")
    private Boolean isRequired;

    @OneToMany(mappedBy = "notificationType")
    private List<Notification> notifications;
}
