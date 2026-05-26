package eu.renovaro.ad.domain;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreelancePackageDto {
    private String packageTitle;
    private String packageDescription;

    private BigDecimal packagePrice;
    private Integer deliveryDays;

    private Integer revisionCount;
    private Integer renderCount;
    private Integer detailDrawingCount;

    private Boolean has3DModel;
    private Boolean has2DDrawings;
    private Boolean hasSourceFile;

    private String packageInfo;
}
