package eu.renovaro.chat.service;

import eu.renovaro.chat.domain.ChatPreviewDto;

import java.util.List;

public interface ChatService {
    List<ChatPreviewDto> getAllUserChats(Long currentUserId);
    Long getOrCreateChat(Long currentUserId, Long otherUserId);
    void toggleIsRead(Long userId, Long chatId, Boolean isRead);
    void toggleIsStarred(Long userId, Long chatId, Boolean isStarred);
    void deleteChat(Long chatId, Long userId);
}
