package eu.renovaro.ad.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageTypeDto {
    private Long packageTypeId;
    private String packageTypeCode;
    private String packageTypeLabel;
}