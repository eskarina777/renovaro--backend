package eu.renovaro.user.service;

import eu.renovaro.user.domain.ProviderProfileDto;
import eu.renovaro.user.domain.UserProfileDto;

public interface UserProfileService {
    UserProfileDto getUserProfileDto(Long userProfileId);
    Long getUserProfileIdByEmail(String email);
    ProviderProfileDto getProviderInfoByEmail(String email);
    void updateShowPhoneNumber(String email, Boolean showPhoneNumber);

}
