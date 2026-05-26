package eu.renovaro.common.config;

import eu.renovaro.user.repository.UserRepository;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import eu.renovaro.common.presence.OnlineUsers;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.Map;

@Component
public class WebSocketEventListener {

    private final OnlineUsers onlineUsers;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    public WebSocketEventListener(
            OnlineUsers onlineUsers,
            SimpMessagingTemplate messagingTemplate,
            UserRepository userRepository
    ) {
        this.onlineUsers = onlineUsers;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        if (event.getUser() == null) return;

        userRepository.findByEmail(event.getUser().getName())
                .ifPresent(user -> {
                    onlineUsers.onlineUsers.remove(user.getUserId().toString());
                    messagingTemplate.convertAndSend(
                            "/topic/online-status",
                            Map.of(
                                    "userId", user.getUserId(),
                                    "onlineStatus", false
                            )
                    );
                });
    }

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        if (event.getUser() == null) return;

        userRepository.findByEmail(event.getUser().getName())
                .ifPresent(user -> {

                    onlineUsers.onlineUsers.add(user.getUserId().toString());

                    messagingTemplate.convertAndSend(
                            "/topic/online-status",
                            Map.of(
                                    "userId", user.getUserId(),
                                    "onlineStatus", true
                            )
                    );
                });
    }




}
