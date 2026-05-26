package eu.renovaro.user.repository;

import eu.renovaro.user.domain.entity.Role;
import eu.renovaro.user.domain.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
Optional<Role> findByRoleName(RoleName roleName);
}
