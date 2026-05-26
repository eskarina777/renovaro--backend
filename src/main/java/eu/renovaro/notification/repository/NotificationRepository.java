package eu.renovaro.notification.repository;

import eu.renovaro.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByRecipientUser_UserIdOrderByCreatedAtDesc(Long userId);
    @Query("""
        SELECT n
        FROM Notification n
        LEFT JOIN FETCH n.notificationType nt
        LEFT JOIN FETCH n.notificationObject no
        LEFT JOIN FETCH no.entityType et
        WHERE n.recipientUser.userId = :userId
        ORDER BY n.createdAt DESC, n.notificationId DESC
    """)
    List<Notification> findAllForUser(@Param("userId") Long userId);

}