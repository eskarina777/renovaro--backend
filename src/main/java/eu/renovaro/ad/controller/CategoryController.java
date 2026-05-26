package eu.renovaro.ad.controller;

import eu.renovaro.ad.domain.AdCardDto;
import eu.renovaro.ad.domain.AdsPageResponse;
import eu.renovaro.ad.domain.CategoryDto;
import eu.renovaro.ad.domain.SubcategoryDto;
import eu.renovaro.ad.service.AdService;
import eu.renovaro.ad.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final AdService adService;

    public CategoryController(CategoryService categoryService,AdService adService) {
        this.categoryService = categoryService;
        this.adService = adService;
    }
    @GetMapping("/subcategory/{id}")
    public SubcategoryDto getSubcategoryById(@PathVariable Long id) {
        return categoryService.getSubcategoryById(id);
    }
    @GetMapping("/most-viewed/subcategories")
    public List<SubcategoryDto> getMostViewedSubcategories() {
        return categoryService.getHomeSubcategories();
    }

    @GetMapping("/{id}/subcategories")
    public List<SubcategoryDto> getSubcategoriesCategory(@PathVariable Long id) {
        return categoryService.getSubcategoriesByCategoryId(id);
    }

    @GetMapping("/{id}/most-viewed-ads")
    public List<AdCardDto>  getMostViewedAdsCategory(@PathVariable Long id) {
        return adService.getMostViewedAdsByCategory(id);
    }

    @GetMapping("/all")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }



}

