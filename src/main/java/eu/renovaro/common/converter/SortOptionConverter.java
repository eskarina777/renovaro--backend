package eu.renovaro.common.converter;

import eu.renovaro.ad.domain.SortOption;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class SortOptionConverter implements Converter<String, SortOption> {
    @Override
    public SortOption convert(String source) {
        return SortOption.valueOf(source.toUpperCase());
    }
}
