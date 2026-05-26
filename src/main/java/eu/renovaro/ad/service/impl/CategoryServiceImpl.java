package eu.renovaro.ad.service.impl;

import eu.renovaro.ad.domain.CategoryDto;
import eu.renovaro.ad.domain.SubcategoryDto;
import eu.renovaro.ad.domain.entity.Category;
import eu.renovaro.ad.domain.entity.Subcategory;
import eu.renovaro.ad.mapper.AdMapper;
import eu.renovaro.ad.repository.CategoryRepository;
import eu.renovaro.ad.repository.SubcategoryRepository;
import eu.renovaro.ad.service.CategoryService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final static AdMapper MAPPER = Mappers.getMapper(AdMapper.class);
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @Override
    public SubcategoryDto getSubcategoryById(Long id) {
        Subcategory subcategory = subcategoryRepository.findById(id)
                .orElseThrow();
        return MAPPER.map(subcategory);
    }

    @Override
    public List<SubcategoryDto> getHomeSubcategories() {

        List<Long> homeSubcategoryIds = List.of(17L, 18L, 9L, 1L, 12L, 11L, 3L, 2L);

        List<Subcategory> subcategories =
                subcategoryRepository.findAllById(homeSubcategoryIds);

        Map<Long, Subcategory> byId = subcategories.stream()
                .collect(Collectors.toMap(Subcategory::getSubcategoryId, s -> s));

        return homeSubcategoryIds.stream()
                .map(byId::get)
                .filter(Objects::nonNull)
                .map(MAPPER::map)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow();
        return MAPPER.map(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(MAPPER::map)
                .toList();
    }

    @Override
     public List<SubcategoryDto> getSubcategoriesByCategoryId(Long categoryId) {
        return subcategoryRepository.findByCategory_CategoryIdOrderBySubcategoryIdAsc(categoryId)
                .stream()
                .map(MAPPER::map)
                .toList();
    }
}
