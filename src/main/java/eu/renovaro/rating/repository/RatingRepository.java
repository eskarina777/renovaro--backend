package eu.renovaro.rating.repository;

import eu.renovaro.rating.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Review, Long> {

    @Query("""
        SELECT r
        FROM Review r
        JOIN r.freelanceRequest fr
        JOIN fr.freelancePackage fp
        JOIN fp.ad a
        JOIN a.userProfile up
        WHERE up.userProfileId = :userProfileId
    """)
    List<Review> findFreelanceReviewsByUserProfileId(
            @Param("userProfileId") Long userProfileId
    );

    @Query("""
        SELECT r
        FROM Review r
        JOIN r.serviceReservation sr
        JOIN sr.serviceTimeSlot sts
        JOIN sts.adServiceTimeSlotList asts
        JOIN asts.ad a
        JOIN a.userProfile up
        WHERE up.userProfileId = :userProfileId
    """)
    List<Review> findLocalFlexReviewsByUserProfileId(
            @Param("userProfileId") Long userProfileId
    );

    @Query("""
    SELECT r.ratingGeneral, COUNT(r)
    FROM Review r
    JOIN r.freelanceRequest fr
    JOIN fr.freelancePackage fp
    JOIN fp.ad a
    JOIN a.userProfile up
    WHERE up.userProfileId = :userProfileId
    GROUP BY r.ratingGeneral
""")
    List<Object[]> countStarsFreelance(@Param("userProfileId") Long userProfileId);

    @Query("""
    SELECT r.ratingGeneral, COUNT(r)
    FROM Review r
    JOIN r.serviceReservation sr
    JOIN sr.serviceTimeSlot sts
    JOIN sts.adServiceTimeSlotList asts
    JOIN asts.ad a
    JOIN a.userProfile up
    WHERE up.userProfileId = :userProfileId
    GROUP BY r.ratingGeneral
""")
    List<Object[]> countStarsLocalFlex(@Param("userProfileId") Long userProfileId);

    @Query("""
    SELECT rc.ratingCriteriaLabel, AVG(rrc.ratingByCriteria)
    FROM ReviewRatingCriteria rrc
    JOIN rrc.review r
    JOIN rrc.ratingCriteria rc
    JOIN r.freelanceRequest fr
    JOIN fr.freelancePackage fp
    JOIN fp.ad a
    JOIN a.userProfile up
    WHERE up.userProfileId = :userProfileId
    GROUP BY rc.ratingCriteriaLabel
""")
    List<Object[]> avgCriteriaFreelance(@Param("userProfileId") Long userProfileId);

    @Query("""
    SELECT rc.ratingCriteriaLabel, AVG(rrc.ratingByCriteria)
    FROM ReviewRatingCriteria rrc
    JOIN rrc.review r
    JOIN rrc.ratingCriteria rc
    JOIN r.serviceReservation sr
    JOIN sr.serviceTimeSlot sts
    JOIN sts.adServiceTimeSlotList asts
    JOIN asts.ad a
    JOIN a.userProfile up
    WHERE up.userProfileId = :userProfileId
    GROUP BY rc.ratingCriteriaLabel
""")
    List<Object[]> avgCriteriaLocalFlex(@Param("userProfileId") Long userProfileId);

}
