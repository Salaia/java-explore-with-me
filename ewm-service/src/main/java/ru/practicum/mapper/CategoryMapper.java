package ru.practicum.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.CategoryCreateDto;
import ru.practicum.model.Category;

@UtilityClass
public class CategoryMapper {
    public static Category toCategory(CategoryCreateDto category) {
        return Category.builder()
                .name(category.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category newCategory) {
        return CategoryDto.builder()
                .id(newCategory.getId())
                .name(newCategory.getName())
                .build();
    }
}