package eu.renovaro.ad.mapper;

import eu.renovaro.ad.domain.*;
import eu.renovaro.ad.domain.entity.*;
import eu.renovaro.user.domain.CityDto;
import eu.renovaro.user.domain.entity.City;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AdMapper {

    CategoryDto map(Category category);
    @Mapping(target = "categoryId", source = "category.categoryId")
    SubcategoryDto map(Subcategory category);
    AdPricingPlanDto map(AdPricingPlan entity);
    PackageTypeDto map(PackageType entity);
    PricingUnitDto map(PricingUnit entity);

}
