package ru.practicum.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.dto.CategoryCreateDto;
import ru.practicum.dto.CategoryDto;
import ru.practicum.model.Category;

@UtilityClass
public class CategoryMapper {
    public Category toCategory(CategoryCreateDto category) {
        return Category.builder()
                .name(category.getName())
                .build();
    }

    public CategoryDto toCategoryDto(Category newCategory) {
        return CategoryDto.builder()
                .id(newCategory.getId())
                .name(newCategory.getName())
                .build();
    }
}