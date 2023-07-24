package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.CategoryCreateDto;
import ru.practicum.exception.ValidationIdException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventService eventService;

    public CategoryDto createCategory(CategoryCreateDto category) {
        Category newCategory = categoryRepository.save(CategoryMapper.toCategory(category));
        return CategoryMapper.toCategoryDto(newCategory);
    }

    public void deleteCategory(Long catId) {
        Category category = checkCategory(catId);
        checkEvent(catId);
        categoryRepository.delete(category);
    }

    public List<CategoryDto> get(Integer from, Integer size) {
        List<Category> categoryList = categoryRepository.findAll(PageRequest.of(from, size)).getContent();
        return categoryList.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto getById(Long catId) {
        Category category = checkCategory(catId);
        return CategoryMapper.toCategoryDto(category);
    }

    public Category checkCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new ValidationIdException("Категория с id=" + catId + ", не найдена"));
    }

    private void checkEvent(Long catId) {
        Event event = eventService.findEventByCategoryId(catId);
        if (event != null) {
            log.warn("Ошибка Категория с ID = {}, не пустая", catId);
            throw new DataIntegrityViolationException("The category is not empty");
        }
    }

    public CategoryDto updateCategory(CategoryCreateDto categoryDto, Long catId) {
        Category category = checkCategory(catId);
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }
}
