package eu.renovaro.notification.domain.entity;

import eu.renovaro.notification.domain.UserNotificationTypeId;
import eu.renovaro.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_notification_type")
@IdClass(UserNotificationTypeId.class)
public class UserNotificationType {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "notification_type_id", nullable = false)
    private Long notificationTypeId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "notification_type_id", referencedColumnName = "notification_type_id", insertable = false, updatable = false)
    private NotificationType notificationType;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    public UserNotificationType(Long userId, Long notificationTypeId, Boolean isEnabled) {
        this.userId = userId;
        this.notificationTypeId = notificationTypeId;
        this.isEnabled = isEnabled;
    }
}
