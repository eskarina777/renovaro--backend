package eu.renovaro.user.controller;

import eu.renovaro.user.domain.ProviderProfileDto;
import eu.renovaro.user.domain.RoleName;
import eu.renovaro.user.domain.UserInfo;
import eu.renovaro.user.domain.UserProfileDto;
import eu.renovaro.user.service.UserProfileService;
import eu.renovaro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserProfileService userProfileService;
    private final UserService userService;
    @GetMapping("/provider-info")
    public ProviderProfileDto getProviderInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userProfileService.getProviderInfoByEmail(email);
    }

    @GetMapping("/user-info")
    public UserInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserInfoByEmail(auth.getName());
    }
    @PostMapping("/add-client-role")
    public void addClientRoleToProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserInfoByEmail(email).getUserId();
        userService.addClientRoleToProvider(userId);
    }
    @PostMapping("/add-provider-role")
    public void addProviderRoleToClient(@RequestBody RoleRequest role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long userId = userService.getUserInfoByEmail(email).getUserId();
        userService.addProviderRoleToClient(userId, role.role);
    }
    public static class RoleRequest {
        public RoleName role;
    }
    @PatchMapping("/toggle-show-phone-number")
    public void updateShowPhoneNumber(@RequestBody Map<String, Boolean> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        userProfileService.updateShowPhoneNumber(email,  request.get("showPhoneNumber"));
    }

}
