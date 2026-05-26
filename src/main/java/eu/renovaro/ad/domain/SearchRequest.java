package eu.renovaro.ad.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private Integer cityId;
    private String text;
}
