package eu.renovaro.ad.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreelanceResponseDto {
    private FreelancePackageDto basic;
    private FreelancePackageDto standard;
    private FreelancePackageDto premium;
}
