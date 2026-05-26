package eu.renovaro.chat.repository;

import eu.renovaro.chat.domain.ChatUserId;
import eu.renovaro.chat.domain.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, ChatUserId> {
    Optional<ChatUser> findByChat_ChatIdAndUser_UserId(Long chatId, Long userId);

}
