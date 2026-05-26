package eu.renovaro.user.service;

import eu.renovaro.auth.domain.RegisterRequest;
import eu.renovaro.user.domain.RoleName;
import eu.renovaro.user.domain.SpecialistRegisterRequest;
import eu.renovaro.user.domain.UserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserInfo getUserInfoByEmail(String email);
    void registerSpecialist(SpecialistRegisterRequest request, MultipartFile file);
    void addClientRoleToProvider(Long providerId);
    void addProviderRoleToClient(Long userId, RoleName role);
    Long getUserIdByEmail(String email);

}
