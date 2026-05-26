package eu.renovaro.user.service.impl;

import eu.renovaro.auth.domain.RegisterRequest;
import eu.renovaro.common.service.CloudinaryService;
import eu.renovaro.notification.service.NotificationService;
import eu.renovaro.user.domain.RoleName;
import eu.renovaro.user.domain.SpecialistPayload;
import eu.renovaro.user.domain.SpecialistRegisterRequest;
import eu.renovaro.user.domain.UserInfo;
import eu.renovaro.user.domain.entity.*;
import eu.renovaro.user.repository.RoleRepository;
import eu.renovaro.user.repository.UserProfileRepository;
import eu.renovaro.user.repository.UserRepository;
import eu.renovaro.user.repository.UserRoleRepository;
import eu.renovaro.user.service.UserService;
import eu.renovaro.user.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final static UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final UserProfileRepository userProfileRepository;
    private final CloudinaryService cloudinaryService;
    private final NotificationService notificationService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository,
                           UserProfileRepository userProfileRepository,
                           CloudinaryService cloudinaryService,
                           NotificationService notificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.userProfileRepository = userProfileRepository;
        this.cloudinaryService = cloudinaryService;
        this.notificationService = notificationService;
    }
    @Override
    public void addClientRoleToProvider(Long providerId) {
        User user = userRepository.findById(providerId)
                .orElseThrow();

        boolean hasClientRole = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getRoleName)
                .anyMatch(r -> r == RoleName.CLIENT);

        if (hasClientRole) {
            return;
        }

        Role clientRole = roleRepository.findByRoleName(RoleName.CLIENT)
                .orElseThrow();

        userRoleRepository.save(
                new UserRole(user.getUserId(), clientRole.getRoleId(), user, clientRole)
        );
    }
    @Override
    public void addProviderRoleToClient(Long userId, RoleName role) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        boolean hasRole = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getRoleName)
                .anyMatch(r -> r == role);

        if (hasRole) {
            return;
        }

        Role providerRole = roleRepository.findByRoleName(role)
                .orElseThrow();

        userRoleRepository.save(
                new UserRole(user.getUserId(), providerRole.getRoleId(), user, providerRole)
        );
    }


    @Override
    public void registerSpecialist(SpecialistRegisterRequest request, MultipartFile file) {
        SpecialistPayload profile = request.getSpecialistProfile();

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setPhoneNumber(profile.getPhone());
        user.setJoinedOn(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            CloudinaryService.UploadResult uploadResult = cloudinaryService.uploadImage(file);
            user.setProfileImageUrl(uploadResult.url());
            user.setProfileImageId(uploadResult.publicId());
        }

        Role role = roleRepository.findByRoleName(RoleName.SPECIALIST)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        User savedUser = userRepository.save(user);

        userRoleRepository.save(new UserRole(savedUser.getUserId(), role.getRoleId(), savedUser, role));

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(savedUser);
        userProfile.setUserDetails(profile.getDescription());
        userProfile.setUserWebsite(profile.getWebsite());
        userProfile.setShowPhoneNumber(Boolean.TRUE.equals(profile.getPublishPhone()));
        userProfileRepository.save(userProfile);
        notificationService.initializeUserNotificationTypes(savedUser.getUserId());

    }

    public UserInfo getUserInfoByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

        String personName = user.getFirstName() + " " + user.getLastName();

        boolean isCompany = user.getUserRoles() != null
                && user.getUserRoles().stream()
                .map(UserRole::getRole)
                .filter(Objects::nonNull)
                .map(Role::getRoleName)
                .anyMatch(RoleName.COMPANY::equals);

        String companyName = Optional.ofNullable(user.getUserProfile())
                .map(UserProfile::getCompany)
                .map(Company::getCompanyName)
                .orElse(null);

        List<RoleName> roles = user.getUserRoles() != null
                ? user.getUserRoles().stream()
                .map(UserRole::getRole)
                .filter(Objects::nonNull)
                .map(Role::getRoleName)
                .toList()
                : List.of();

        String displayName = isCompany && companyName != null ? companyName : personName;

        return new UserInfo(
                user.getUserId(),
                displayName,
                roles,
                user.getProfileImageUrl()
        );
    }
    @Override
    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow()
                .getUserId();
    }

}
