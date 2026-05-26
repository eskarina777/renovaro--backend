package eu.renovaro.ad.service;

import eu.renovaro.ad.domain.CategoryDto;
import eu.renovaro.ad.domain.SubcategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    List<SubcategoryDto> getSubcategoriesByCategoryId(Long categoryId);
    CategoryDto getCategoryById(Long id);
    List<SubcategoryDto> getHomeSubcategories();
    SubcategoryDto getSubcategoryById(Long id);

}
