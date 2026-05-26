package eu.renovaro.chat.domain.entity;

import eu.renovaro.chat.domain.ChatUserId;
import eu.renovaro.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chat_user")
@IdClass(ChatUserId.class)
public class ChatUser {

    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "joined_on")
    private LocalDateTime joinedOn;

    @Column(name = "left_on")
    private LocalDateTime leftOn;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "is_starred")
    private Boolean isStarred;

    @ManyToOne
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public ChatUser(Long chatId, Long userId) {
        this.chatId = chatId;
        this.userId = userId;
    }
}
