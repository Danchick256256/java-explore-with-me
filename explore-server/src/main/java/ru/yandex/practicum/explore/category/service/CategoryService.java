package ru.yandex.practicum.explore.category.service;

import ru.yandex.practicum.explore.category.model.Category;
import ru.yandex.practicum.explore.category.dto.CategoryDto;
import ru.yandex.practicum.explore.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAll(Integer from, Integer size);

    Category findById(Long catId);

    CategoryDto create(NewCategoryDto body);

    CategoryDto update(CategoryDto body);

    void deleteById(Long catId);
}
