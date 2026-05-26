package eu.renovaro.notification.service.impl;

import eu.renovaro.notification.domain.EntityTypeName;
import eu.renovaro.notification.domain.NotificationPreviewDto;
import eu.renovaro.notification.domain.NotificationTypeName;
import eu.renovaro.notification.domain.entity.*;
import eu.renovaro.notification.mapper.NotificationMapper;
import eu.renovaro.notification.repository.*;
import eu.renovaro.notification.service.NotificationService;
import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final NotificationMapper MAPPER = Mappers.getMapper(NotificationMapper.class);


    private final NotificationTypeRepository notificationTypeRepository;
    private final NotificationRepository notificationRepository;
    private final UserNotificationTypeRepository userNotificationTypeRepository;
    private final UserRepository userRepository;
    private final EntityTypeRepository entityTypeRepository;
    private final SimpMessagingTemplate  simpMessagingTemplate;
    private final NotificationObjectRepository notificationObjectRepository;

    public NotificationServiceImpl(
            NotificationTypeRepository notificationTypeRepository,
            NotificationRepository notificationRepository,
            UserNotificationTypeRepository userNotificationTypeRepository,
            UserRepository userRepository,
            EntityTypeRepository entityTypeRepository,
            SimpMessagingTemplate simpMessagingTemplate,
            NotificationObjectRepository notificationObjectRepository
    ) {
        this.notificationTypeRepository = notificationTypeRepository;
        this.userNotificationTypeRepository = userNotificationTypeRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.entityTypeRepository = entityTypeRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationObjectRepository =  notificationObjectRepository;
    }

    @Override
    public List<NotificationPreviewDto> getAllNotifications(Long userId) {
        return notificationRepository.findAllForUser(userId)
                .stream()
                .map(this::toPreviewDto)
                .toList();

    }

    @Override
    public void initializeUserNotificationTypes(Long userId) {
        if (!userNotificationTypeRepository.findAllByUserId(userId).isEmpty()) {
            return;
        }

        List<UserNotificationType> rows = notificationTypeRepository.findAll()
                .stream()
                .map(type -> new UserNotificationType(userId, type.getNotificationTypeId(), true))
                .toList();

        userNotificationTypeRepository.saveAll(rows);
    }
    @Override
    public void createNotification(
            Long recipientUserId,
            NotificationTypeName notificationTypeName,
            EntityTypeName entityTypeName,
            Long entityId,
            String notificationText
    ) {
        boolean enabled = userNotificationTypeRepository.isEnabledForUser(recipientUserId, notificationTypeName);
        if (!enabled) {
            return;
        }

        User recipientUser = userRepository.findById(recipientUserId)
                .orElseThrow();

        NotificationType notificationType = notificationTypeRepository
                .findByNotificationTypeName(notificationTypeName)
                .orElseThrow();

        EntityType entityType = entityTypeRepository
                .findByEntityTypeName(entityTypeName)
                .orElseThrow();

        LocalDateTime now = LocalDateTime.now();

        NotificationObject notificationObject = new NotificationObject();
        notificationObject.setEntityId(entityId);
        notificationObject.setCreatedAt(now);
        notificationObject.setEntityType(entityType);
        notificationObject.setCreatedByUser(recipientUser);
        notificationObjectRepository.save(notificationObject);

        Notification notification = new Notification();
        notification.setNotificationText(notificationText);
        notification.setCreatedAt(now);
        notification.setIsRead(false);
        notification.setNotificationType(notificationType);
        notification.setNotificationObject(notificationObject);
        notification.setRecipientUser(recipientUser);
        Notification saved = notificationRepository.save(notification);
        NotificationPreviewDto dto = toPreviewDto(saved);
        simpMessagingTemplate.convertAndSend("/topic/notifications/" + recipientUserId, dto);
    }


    private NotificationPreviewDto toPreviewDto(Notification notification) {
        NotificationPreviewDto dto = new NotificationPreviewDto();
        dto.setNotificationId(notification.getNotificationId());
        dto.setNotificationText(notification.getNotificationText());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setIsRead(Boolean.TRUE.equals(notification.getIsRead()));

        if (notification.getNotificationType() != null) {
            dto.setNotificationTitle(notification.getNotificationType().getNotificationTitle());
            dto.setNotificationTypeCode(notification.getNotificationType().getNotificationTypeCode());
        }

        if (notification.getNotificationObject() != null) {
            dto.setEntityId(notification.getNotificationObject().getEntityId());
            if (notification.getNotificationObject().getEntityType() != null) {
                dto.setEntityType(
                        notification.getNotificationObject().getEntityType().getEntityTypeName()
                );
            }
        }
        return dto;
    }

    @Override
    public void toggleIsRead(Long notificationId, Boolean isRead) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.setIsRead(Boolean.TRUE.equals(isRead));
                    notificationRepository.save(notification);
                });
    }


    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

}