package eu.renovaro.chat.repository;

import eu.renovaro.chat.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChat_ChatIdOrderByCreatedAtAsc(Long chatId);
    Optional<Message> findFirstByChat_ChatIdOrderByCreatedAtDescMessageIdDesc(Long chatId);
    List<Message> findByChat_ChatIdOrderByCreatedAtAscMessageIdAsc(Long chatId);
}

