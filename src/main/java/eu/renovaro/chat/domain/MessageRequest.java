package eu.renovaro.chat.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private Long chatId;
    private String messageBody;
}
