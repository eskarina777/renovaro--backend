package eu.renovaro.notification.service;

import eu.renovaro.notification.domain.EntityTypeName;
import eu.renovaro.notification.domain.NotificationPreviewDto;
import eu.renovaro.notification.domain.NotificationTypeName;

import java.util.List;

public interface NotificationService {
    List<NotificationPreviewDto> getAllNotifications(Long userId);
    void toggleIsRead(Long notificationId, Boolean isRead);
    void deleteNotification(Long notificationId);
    void initializeUserNotificationTypes(Long userId);
    void createNotification(
            Long recipientUserId,
            NotificationTypeName notificationTypeName,
            EntityTypeName entityTypeName,
            Long entityId,
            String notificationText
    );


}