package eu.renovaro.user.mapper;

import eu.renovaro.auth.domain.RegisterRequest;
import eu.renovaro.user.domain.*;
import eu.renovaro.user.domain.entity.*;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.*;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {
    @Mapping(target = "userWebsite", source = "userWebsite")
    @Mapping(target = "showPhoneNumber", source = "showPhoneNumber")
    @Mapping(target = "totalCredit", source = "creditBalance")
    @Mapping(target = "memberSince", expression = "java(userProfile.getUser() != null && userProfile.getUser().getJoinedOn() != null ? userProfile.getUser().getJoinedOn().toString() : null)")
    @Mapping(target = "phoneNumber", expression = "java(userProfile.getUser() != null ? userProfile.getUser().getPhoneNumber() : null)")
    ProviderProfileDto map(UserProfile userProfile);
    User map(RegisterRequest registerRequest);
    User copyUser(User user);
    CityDto map(City city);

}
