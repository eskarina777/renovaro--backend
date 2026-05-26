package eu.renovaro.chat.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatPreviewDto {
    private Long chatId;

    private Long userId;
    private String userName;
    private String profileImageUrl;
    private Boolean onlineStatus;
    private Long lastMessageId;
    private String lastMessageText;
    private String createdAt;
    private Boolean isRead;
    private Boolean isStarred;
}
