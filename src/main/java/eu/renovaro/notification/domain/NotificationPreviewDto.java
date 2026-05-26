package eu.renovaro.notification.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreviewDto {
    private Long notificationId;
    private Long entityId;
    private String notificationTitle;
    private String notificationText;
    private String notificationTypeCode;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private EntityTypeName entityType;
}
