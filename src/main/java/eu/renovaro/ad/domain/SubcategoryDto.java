package eu.renovaro.ad.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryDto {
    private Long categoryId;
    private Long subcategoryId;
    private String subcategoryCode;
    private String subcategoryName;
}
