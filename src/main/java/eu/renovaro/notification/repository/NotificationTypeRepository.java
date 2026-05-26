package eu.renovaro.notification.repository;

import eu.renovaro.notification.domain.NotificationTypeName;
import eu.renovaro.notification.domain.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Integer> {
    Optional<NotificationType> findByNotificationTypeName(NotificationTypeName notificationTypeName);
}