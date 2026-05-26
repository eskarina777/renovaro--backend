package eu.renovaro.notification.scheduler;


import eu.renovaro.ad.domain.entity.Ad;
import eu.renovaro.ad.repository.AdRepository;
import eu.renovaro.notification.domain.NotificationTypeName;
import eu.renovaro.notification.domain.EntityTypeName;
import eu.renovaro.notification.service.NotificationService;
import eu.renovaro.user.repository.UserProfileRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class AdExpirationScheduler {

    private final AdRepository adRepository;
    private final UserProfileRepository userProfileRepository;
    private final NotificationService notificationService;

    public AdExpirationScheduler(AdRepository adRepository,
                                 UserProfileRepository userProfileRepository,
                                 NotificationService notificationService) {
        this.adRepository = adRepository;
        this.userProfileRepository = userProfileRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void checkExpiringAds() {
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(3);

        List<Ad> expiringAds = adRepository.findByAdExpirationDateBetween(today, limit);

        for (Ad ad : expiringAds) {
            Long userId = userProfileRepository.findUserIdByUserProfileId(
                    ad.getUserProfile().getUserProfileId()
            );

            String text = "Обявата Ви \"" + ad.getTitle() + "\" изтича след 3 дни.";

            notificationService.createNotification(
                    userId,
                    NotificationTypeName.EXPIRING_AD,
                    EntityTypeName.AD,
                    ad.getAdId(),
                    text
            );
        }
    }
}
