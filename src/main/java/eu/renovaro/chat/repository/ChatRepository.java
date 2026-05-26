package eu.renovaro.chat.repository;

import eu.renovaro.chat.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByChatUserList_User_UserId(Long userId);


    @Query("""
    SELECT c FROM Chat c
    JOIN c.chatUserList cu1
    JOIN c.chatUserList cu2
    WHERE cu1.user.userId = :currentUserId
    AND cu2.user.userId = :otherUserId
    """)
    Optional<Chat> findExistingChat(
            @Param("currentUserId") Long currentUserId,
            @Param("otherUserId") Long otherUserId
    );


}
