package eu.renovaro.common.converter;

import eu.renovaro.ad.domain.ProviderRole;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProviderRoleConverter implements Converter<String, ProviderRole> {

    @Override
    public ProviderRole convert(String source) {
        return ProviderRole.valueOf(source.toUpperCase());
    }
}
