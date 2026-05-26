package eu.renovaro.user.repository;

import eu.renovaro.user.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("""
        SELECT up
        FROM UserProfile up
        JOIN FETCH up.user u
        LEFT JOIN FETCH up.company c
        LEFT JOIN FETCH u.userRoles ur
        LEFT JOIN FETCH ur.role r
        WHERE up.userProfileId = :id
    """)
    Optional<UserProfile> getDetails(@Param("id") Long id);
    @Query("""
    SELECT up.userProfileId
    FROM UserProfile up
    JOIN up.user u
    WHERE u.email = :email
""")
    Optional<Long> findUserProfileIdByEmail(@Param("email") String email);
    @Query("""
    SELECT up
    FROM UserProfile up
    JOIN FETCH up.user u
    WHERE u.email = :email
""")
    Optional<UserProfile> findByUserEmail(@Param("email") String email);
    @Query("""
        SELECT up.user.userId
        FROM UserProfile up
        WHERE up.userProfileId = :userProfileId
    """)
    Long findUserIdByUserProfileId(@Param("userProfileId") Long userProfileId);
}
