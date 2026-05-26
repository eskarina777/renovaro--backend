package eu.renovaro.ad.domain;

import eu.renovaro.user.domain.UserProfileDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdDetailsResponseDto {
    private Long adId;
    private ServiceTypeCode serviceTypeCode;
    private SubcategoryDto subcategory;
    private CategoryDto category;
    private UserProfileDto userProfile;
    private AdDetailsDto adDetails;
}
