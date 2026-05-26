package eu.renovaro.user.service.impl;

import eu.renovaro.user.domain.CityDto;
import eu.renovaro.user.mapper.UserMapper;
import eu.renovaro.user.repository.CityRepository;
import eu.renovaro.user.service.CityService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final static UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<CityDto> getAll() {
        return cityRepository.findAll().stream()
                .map (MAPPER::map)
                .toList();
    }
}

