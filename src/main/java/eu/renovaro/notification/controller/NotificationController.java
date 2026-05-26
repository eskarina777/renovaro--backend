package eu.renovaro.notification.controller;

import eu.renovaro.notification.domain.NotificationPreviewDto;
import eu.renovaro.notification.service.NotificationService;
import eu.renovaro.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(
            NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/user-notifications")
    public List<NotificationPreviewDto> getUserNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserInfoByEmail(email).getUserId();
        return notificationService.getAllNotifications(userId);
    }

    @DeleteMapping("/delete/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }

    @PatchMapping("/{notificationId}/toggle-is-read")
    public void toggleIsRead(
            @PathVariable Long notificationId,
            @RequestBody Map<String, Boolean> request
    ) {
        Boolean isRead = request.get("isRead");
        notificationService.toggleIsRead(notificationId, isRead);
    }
}