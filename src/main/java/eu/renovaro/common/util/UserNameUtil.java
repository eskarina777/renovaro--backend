package eu.renovaro.common.util;

import eu.renovaro.user.domain.RoleName;
import eu.renovaro.user.domain.entity.Role;
import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.domain.entity.UserRole;

import java.util.Optional;

public class UserNameUtil {

    public static String constructUserName(User user) {
        if (user == null) {
            return null;
        }

        boolean isCompany = user.getUserRoles() != null
                && user.getUserRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getRoleName)
                .anyMatch(RoleName.COMPANY::equals);

        if (isCompany && user.getUserProfile() != null && user.getUserProfile().getCompany() != null) {
            return user.getUserProfile().getCompany().getCompanyName();
        }

        String firstName = Optional.ofNullable(user.getFirstName()).orElse("");
        String lastName = Optional.ofNullable(user.getLastName()).orElse("");

        String fullName = (firstName + " " + lastName).trim();
        return fullName.isEmpty() ? null : fullName;
    }
}













