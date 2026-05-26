package eu.renovaro.user.service.impl;

import eu.renovaro.ad.mapper.AdMapper;
import eu.renovaro.user.domain.ProviderProfileDto;
import eu.renovaro.user.domain.RoleName;
import eu.renovaro.user.domain.UserProfileDto;
import eu.renovaro.user.domain.entity.*;
import eu.renovaro.user.mapper.UserMapper;
import eu.renovaro.user.repository.UserProfileRepository;
import eu.renovaro.user.service.UserProfileService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final static UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }
    @Override
    public ProviderProfileDto getProviderInfoByEmail(String email) {
        UserProfile userProfile = userProfileRepository.findByUserEmail(email)
                .orElseThrow();

        return MAPPER.map(userProfile);
    }
    @Override
    public Long getUserProfileIdByEmail(String email) {
        return userProfileRepository.findUserProfileIdByEmail(email)
                .orElseThrow();
    }


    @Override
    public UserProfileDto getUserProfileDto(Long userProfileId) {
        UserProfile profile = userProfileRepository.getDetails(userProfileId).orElseThrow();

        User user = profile.getUser();
        List<RoleName> roles = (user != null && user.getUserRoles() != null)
                ? user.getUserRoles().stream()
                .map(UserRole::getRole)
                .filter(Objects::nonNull)
                .map(Role::getRoleName)
                .filter(Objects::nonNull)
                .toList()
                : List.of();

        boolean isCompany = roles.contains(RoleName.COMPANY);

        String userName;
        if (isCompany) {
            userName = profile.getCompany() != null ? profile.getCompany().getCompanyName() : null;
        } else {
            String first = user != null && user.getFirstName() != null ? user.getFirstName() : "";
            String last = user != null && user.getLastName() != null ? user.getLastName() : "";
            String full = (first + " " + last).trim();
            userName = full.isEmpty() ? null : full;
        }

        String memberSince = (user != null && user.getJoinedOn() != null) ? user.getJoinedOn().toString() : null;

        return new UserProfileDto(
                user != null ? user.getUserId() : null,
                userName,
                roles,
                user != null ? user.getProfileImageUrl() : null,
                profile.getUserWebsite(),
                profile.getUserDetails(),
                profile.getRatingAverage(),
                profile.getRatingCount(),
                memberSince,
                user != null ? user.getPhoneNumber() : null,
                profile.getShowPhoneNumber()
        );
    }

    @Override
    public void updateShowPhoneNumber(String email, Boolean showPhoneNumber) {
        UserProfile userProfile = userProfileRepository.findByUserEmail(email)
                .orElseThrow();
        userProfile.setShowPhoneNumber(showPhoneNumber);
        userProfileRepository.save(userProfile);
    }

}
