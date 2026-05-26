package eu.renovaro.chat.service.impl;

import eu.renovaro.chat.domain.ChatPreviewDto;
import eu.renovaro.chat.domain.entity.Chat;
import eu.renovaro.chat.domain.entity.ChatUser;
import eu.renovaro.chat.domain.entity.Message;
import eu.renovaro.chat.mapper.ChatMapper;
import eu.renovaro.chat.repository.ChatRepository;
import eu.renovaro.chat.repository.ChatUserRepository;
import eu.renovaro.chat.repository.MessageRepository;
import eu.renovaro.chat.service.ChatService;
import eu.renovaro.chat.service.MessageService;
import eu.renovaro.user.domain.RoleName;
import eu.renovaro.user.domain.entity.*;
import eu.renovaro.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import eu.renovaro.common.presence.OnlineUsers;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final SimpUserRegistry userRegistry;
    private final MessageService messageService;
    private final OnlineUsers onlineUsers;
    private final MessageRepository messageRepository;

    private static final ChatMapper MAPPER = Mappers.getMapper(ChatMapper.class);

    public ChatServiceImpl(
            ChatRepository chatRepository,
            ChatUserRepository chatUserRepository,
            UserRepository userRepository,
            SimpUserRegistry userRegistry,
            MessageService messageService,
            OnlineUsers onlineUsers,
            MessageRepository messageRepository
    ) {
        this.chatRepository = chatRepository;
        this.chatUserRepository = chatUserRepository;
        this.userRepository = userRepository;
        this.userRegistry = userRegistry;
        this.messageService = messageService;
        this.onlineUsers = onlineUsers;
        this.messageRepository = messageRepository;
    }

    @Override
    public List<ChatPreviewDto> getAllUserChats(Long currentUserId) {
        List<Chat> chats = chatRepository.findAllByChatUserList_User_UserId(currentUserId);

        return chats.stream()
                .map(chat -> {
                    ChatPreviewDto dto = mapChatToChatPreview(chat, currentUserId);

                    messageRepository
                            .findFirstByChat_ChatIdOrderByCreatedAtDescMessageIdDesc(chat.getChatId())
                            .ifPresent(lastMessage -> {
                                dto.setLastMessageId(lastMessage.getMessageId());
                                dto.setLastMessageText(lastMessage.getMessageBody());
                                dto.setCreatedAt(
                                        lastMessage.getCreatedAt() != null
                                                ? lastMessage.getCreatedAt().toString()
                                                : null
                                );
                            });

                    return dto;
                })
                .sorted(
                        Comparator
                                .comparing(
                                        ChatPreviewDto::getCreatedAt,
                                        Comparator.nullsLast(String::compareTo)
                                )
                                .reversed()
                                .thenComparing(
                                        ChatPreviewDto::getChatId,
                                        Comparator.nullsLast(Long::compareTo).reversed()
                                )
                )
                .toList();
    }

    private ChatPreviewDto mapChatToChatPreview(Chat chat, Long currentUserId) {
        ChatPreviewDto dto = new ChatPreviewDto();
        dto.setChatId(chat.getChatId());

        User otherUser = findOtherUser(chat, currentUserId);
        if (otherUser != null) {
            dto.setUserId(otherUser.getUserId());
            dto.setProfileImageUrl(otherUser.getProfileImageUrl());
            dto.setOnlineStatus(
                    onlineUsers.onlineUsers.contains(otherUser.getUserId().toString())
            );
            String userName = otherUser.getFirstName() + " " + otherUser.getLastName();

            boolean isCompany = otherUser.getUserRoles() != null
                    && otherUser.getUserRoles().stream()
                    .map(UserRole::getRole)
                    .map(Role::getRoleName)
                    .anyMatch(RoleName.COMPANY::equals);

            String companyName = Optional.ofNullable(otherUser.getUserProfile())
                    .map(UserProfile::getCompany)
                    .map(Company::getCompanyName)
                    .orElse(null);

            dto.setUserName(isCompany && companyName != null ? companyName : userName);

        }

        Message lastMessage = findLastMessage(chat);
        if (lastMessage != null) {
            dto.setLastMessageId(lastMessage.getMessageId());
            dto.setLastMessageText(lastMessage.getMessageBody());
            dto.setCreatedAt(lastMessage.getCreatedAt().toString());
        } else {
            dto.setCreatedAt(null);
        }

        ChatUser currentChatUser = findUsersInChat(chat, currentUserId);
        dto.setIsStarred(currentChatUser != null && Boolean.TRUE.equals(currentChatUser.getIsStarred()));
        dto.setIsRead(currentChatUser != null && Boolean.TRUE.equals(currentChatUser.getIsRead()));

        return dto;
    }

    @Override
    public Long getOrCreateChat(Long currentUserId, Long otherUserId) {
        if (currentUserId.equals(otherUserId)) {
            throw new IllegalArgumentException();
        }

        return chatRepository.findExistingChat(currentUserId, otherUserId)
                .map(Chat::getChatId)
                .orElseGet(() ->
                        createChatWithTwoUsers(currentUserId, otherUserId).getChatId()
                );
    }

    private Chat createChatWithTwoUsers(Long currentUserId, Long otherUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + currentUserId));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + otherUserId));
        LocalDateTime now = LocalDateTime.now();
        Chat newChat = new Chat();
        newChat.setCreatedAt(LocalDateTime.now());
        Chat savedChat = chatRepository.save(newChat);

        ChatUser currentChatUser = new ChatUser(savedChat.getChatId(), currentUserId);
        currentChatUser.setChat(savedChat);
        currentChatUser.setUser(currentUser);
        currentChatUser.setJoinedOn(now);
        currentChatUser.setIsRead(false);
        currentChatUser.setIsStarred(false);

        ChatUser otherChatUser = new ChatUser(savedChat.getChatId(), otherUserId);
        otherChatUser.setChat(savedChat);
        otherChatUser.setUser(otherUser);
        otherChatUser.setJoinedOn(now);
        otherChatUser.setIsRead(false);
        otherChatUser.setIsStarred(false);

        chatUserRepository.save(currentChatUser);
        chatUserRepository.save(otherChatUser);

        savedChat.setChatUserList(List.of(currentChatUser, otherChatUser));
        return savedChat;
    }

    private ChatUser findUsersInChat(Chat chat, Long userId) {
        if (chat.getChatUserList() == null) return null;
        return chat.getChatUserList().stream()
                .filter(chatUser -> chatUser.getUser() != null && userId.equals(chatUser.getUser().getUserId()))
                .findFirst()
                .orElse(null);
    }

    private User findOtherUser(Chat chat, Long currentUserId) {
        if (chat.getChatUserList() == null) return null;
        return chat.getChatUserList().stream()
                .map(ChatUser::getUser)
                .filter(user -> user != null && !user.getUserId().equals(currentUserId))
                .findFirst()
                .orElse(null);
    }

    private Message findLastMessage(Chat chat) {
        if (chat.getMessageList() == null || chat.getMessageList().isEmpty()) return null;
        return chat.getMessageList().stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .orElse(null);
    }


    @Override
    public void toggleIsRead(Long userId, Long chatId, Boolean isRead) {
        chatUserRepository.findByChat_ChatIdAndUser_UserId(chatId, userId)
                .ifPresent(chatUser -> {
                    chatUser.setIsRead(Boolean.TRUE.equals(isRead));
                    chatUserRepository.save(chatUser);
                });
    }

    @Override
    public void toggleIsStarred(Long userId, Long chatId, Boolean isStarred) {
        chatUserRepository.findByChat_ChatIdAndUser_UserId(chatId, userId)
                .ifPresent(chatUser -> {
                    chatUser.setIsStarred(Boolean.TRUE.equals(isStarred));
                    chatUserRepository.save(chatUser);
                });

    }


    @Override
    public void deleteChat(Long chatId, Long userId) {
        chatUserRepository.findByChat_ChatIdAndUser_UserId(chatId, userId)
                .ifPresent(chatUserRepository::delete);
    }
}
