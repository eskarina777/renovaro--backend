package eu.renovaro.ad.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdsPageResponse {

    private List<AdCardDto> ads;
    private long total;
}