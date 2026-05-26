package eu.renovaro.notification.domain.entity;

import eu.renovaro.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "notification_text")
    private String notificationText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_read")
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "notification_type_id")
    private NotificationType notificationType;

    @ManyToOne
    @JoinColumn(name = "notification_object_id")
    private NotificationObject notificationObject;

    @ManyToOne
    @JoinColumn(name = "recipient_user_id")
    private User recipientUser;

}
