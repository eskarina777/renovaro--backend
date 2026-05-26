package eu.renovaro.chat.controller;

import eu.renovaro.chat.domain.ChatPreviewDto;
import eu.renovaro.chat.domain.MessageDto;
import eu.renovaro.chat.service.ChatService;
import eu.renovaro.chat.service.MessageService;
import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.repository.UserRepository;
import eu.renovaro.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserRepository userRepository,
                          MessageService messageService, UserService userService) {
        this.chatService = chatService;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/user-chats")
    public List<ChatPreviewDto> getAllUserChats(Principal principal) {
        String email = principal.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return chatService.getAllUserChats(currentUser.getUserId());
    }

    @PostMapping("/create/{otherUserId}")
    public Long createChat(@PathVariable Long otherUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Long currentUserId = userService.getUserInfoByEmail(email).getUserId();
        return chatService.getOrCreateChat(currentUserId, otherUserId);
    }

    @GetMapping("/{chatId}/messages")
    public List<MessageDto> getChatMessages(@PathVariable Long chatId) {
        return messageService.getChatMessages(chatId);
    }

    @PostMapping("/delete/{chatId}")
    public void deleteChat(@PathVariable Long chatId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long currentUserId = userService.getUserInfoByEmail(email).getUserId();
        chatService.deleteChat(chatId, currentUserId);
    }

    @PatchMapping("/toggle-is-read/{chatId}")
    public void toggleIsRead(@PathVariable Long chatId, @RequestBody Map<String, Boolean> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        chatService.toggleIsRead(user.getUserId(), chatId, request.get("isRead"));
    }


    @PatchMapping("/toggle-is-starred/{chatId}")
    public void toggleIsStarred(@PathVariable Long chatId, @RequestBody Map<String, Boolean> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();

        chatService.toggleIsStarred(user.getUserId(), chatId, request.get("isStarred"));
    }
}
