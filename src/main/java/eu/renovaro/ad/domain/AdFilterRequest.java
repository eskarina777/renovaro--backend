package eu.renovaro.ad.domain;

import eu.renovaro.user.domain.RoleName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdFilterRequest {
    private Long cityId;
    private Integer rating;
    private Integer budgetMin;
    private Integer budgetMax;
    private String search;
    private ProviderRole publishedBy;
    private SortOption sortBy;

}
