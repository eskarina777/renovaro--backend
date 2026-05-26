package eu.renovaro.notification.repository;

import eu.renovaro.notification.domain.entity.NotificationObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationObjectRepository extends JpaRepository<NotificationObject, Long> {
}