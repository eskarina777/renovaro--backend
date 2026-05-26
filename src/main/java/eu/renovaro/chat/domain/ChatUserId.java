package eu.renovaro.chat.domain;

import java.io.Serializable;
import java.util.Objects;

public class ChatUserId implements Serializable {

    private Long chatId;
    private Long userId;

    public ChatUserId() {}

    public ChatUserId(Long chatId, Long userId) {
        this.chatId = chatId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || org.hibernate.Hibernate.getClass(this) != org.hibernate.Hibernate.getClass(o)) return false;
        ChatUserId that = (ChatUserId) o;
        return Objects.equals(chatId, that.chatId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userId);
    }
}

