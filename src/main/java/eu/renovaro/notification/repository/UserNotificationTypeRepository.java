package eu.renovaro.notification.repository;

import eu.renovaro.notification.domain.NotificationTypeName;
import eu.renovaro.notification.domain.UserNotificationTypeId;
import eu.renovaro.notification.domain.entity.UserNotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserNotificationTypeRepository extends JpaRepository<UserNotificationType, UserNotificationTypeId> {

    List<UserNotificationType> findAllByUserId(Long userId);
    @Query("""
        SELECT CASE WHEN COUNT(unt) > 0 THEN TRUE ELSE FALSE END
        FROM UserNotificationType unt
        JOIN unt.notificationType nt
        WHERE unt.user.userId = :userId
          AND nt.notificationTypeName = :typeName
          AND COALESCE(unt.isEnabled, FALSE) = TRUE
    """)
    boolean isEnabledForUser(
            @Param("userId") Long userId,
            @Param("typeName") NotificationTypeName typeName
    );
    boolean existsByUserIdAndNotificationTypeId(Long userId, Integer notificationTypeId);

    void deleteByUserIdAndNotificationTypeId(Long userId, Integer notificationTypeId);
}
