package eu.renovaro.ad.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdImageUrlDto {
    private Long adImageId;
    private Boolean isPrimary;
    private String adImageUrl;
}
