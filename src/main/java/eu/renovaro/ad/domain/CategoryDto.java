package eu.renovaro.ad.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Integer categoryId;
    private String categoryCode;
    private String categoryName;
}
