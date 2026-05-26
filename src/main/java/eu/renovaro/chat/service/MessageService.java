package eu.renovaro.chat.service;

import eu.renovaro.chat.domain.MessageDto;
import eu.renovaro.chat.domain.entity.Chat;

import java.util.List;

public interface MessageService {
    MessageDto sendMessage(Long userId, Long chatId, String messageBody);
    List<MessageDto> getChatMessages(Long chatId);
}
