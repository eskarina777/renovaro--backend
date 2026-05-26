package eu.renovaro.chat.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private Long toChatId;
    private Long messageId;
    private Long fromUserId;
    private String messageBody;
    private LocalDateTime createdAt;
}

