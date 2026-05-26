package eu.renovaro.chat.service.impl;

import eu.renovaro.chat.domain.MessageDto;
import eu.renovaro.chat.domain.entity.Chat;
import eu.renovaro.chat.domain.entity.Message;
import eu.renovaro.chat.mapper.ChatMapper;
import eu.renovaro.chat.repository.MessageRepository;
import eu.renovaro.chat.service.MessageService;
import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private static final ChatMapper MAPPER = Mappers.getMapper(ChatMapper.class);

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MessageDto sendMessage(Long fromUserId, Long chatId, String messageBody) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + fromUserId));

        Chat chat = new Chat();
        chat.setChatId(chatId);

        Message message = new Message();
        message.setUser(fromUser);
        message.setChat(chat);
        message.setMessageBody(messageBody);
        message.setCreatedAt(LocalDateTime.now());

        Message saved = messageRepository.save(message);
        return MAPPER.map(saved);
    }
    @Override
    public List<MessageDto> getChatMessages(Long chatId) {
        return messageRepository
                .findByChat_ChatIdOrderByCreatedAtAscMessageIdAsc(chatId)
                .stream()
                .map(MAPPER::map)
                .toList();
    }


}
