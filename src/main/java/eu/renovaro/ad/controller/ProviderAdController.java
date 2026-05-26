package eu.renovaro.ad.controller;

import eu.renovaro.ad.domain.AdCardDto;
import eu.renovaro.ad.service.AdService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ads")
public class ProviderAdController {

    private final AdService adService;

    public ProviderAdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("/provider-ads")
    public List<AdCardDto> getProviderAds() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return adService.getProviderAdsByEmail(email);
    }
}
