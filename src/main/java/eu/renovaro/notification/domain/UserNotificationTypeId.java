package eu.renovaro.notification.domain;

import java.io.Serializable;
import java.util.Objects;

public class UserNotificationTypeId implements Serializable {

    private Long userId;
    private Long notificationTypeId;

    public UserNotificationTypeId() {}

    public UserNotificationTypeId(Long userId, Long notificationTypeId) {
        this.userId = userId;
        this.notificationTypeId = notificationTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || org.hibernate.Hibernate.getClass(this) != org.hibernate.Hibernate.getClass(o)) return false;
        UserNotificationTypeId that = (UserNotificationTypeId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(notificationTypeId, that.notificationTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, notificationTypeId);
    }
}
