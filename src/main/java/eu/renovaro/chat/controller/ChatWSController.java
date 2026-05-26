package eu.renovaro.chat.controller;

import org.springframework.context.annotation.Lazy;
import eu.renovaro.chat.domain.MessageDto;
import eu.renovaro.chat.domain.MessageRequest;
import eu.renovaro.chat.service.MessageService;
import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.repository.UserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWSController {

    private final  SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserRepository userRepository;

    public ChatWSController(SimpMessagingTemplate messagingTemplate,
                            MessageService messageService,
                            UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat/send")
    public void sendMessage(MessageRequest request, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        Long userId = user.getUserId();

        MessageDto messageDto = messageService.sendMessage(userId, request.getChatId(), request.getMessageBody());

        messagingTemplate.convertAndSend("/topic/chat/" + request.getChatId(), messageDto);

    }

}
