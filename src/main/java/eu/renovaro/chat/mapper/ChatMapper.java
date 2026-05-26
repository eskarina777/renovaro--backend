package eu.renovaro.chat.mapper;

import eu.renovaro.chat.domain.MessageDto;
import eu.renovaro.chat.domain.entity.Message;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.*;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ChatMapper {

    @Mapping(target = "toChatId", source = "chat.chatId")
    @Mapping(target = "fromUserId", source = "user.userId")
    MessageDto map(Message message);

}