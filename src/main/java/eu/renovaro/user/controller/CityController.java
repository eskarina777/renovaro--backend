package eu.renovaro.user.controller;

import eu.renovaro.user.domain.CityDto;
import eu.renovaro.user.service.CityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public/user")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/cities")
    public List<CityDto> getAll() {
        return cityService.getAll();
    }
}
