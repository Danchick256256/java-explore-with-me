package ru.yandex.practicum.explore.category.service;

import ru.yandex.practicum.explore.category.model.Category;
import ru.yandex.practicum.explore.category.dto.CategoryDto;
import ru.yandex.practicum.explore.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    List<Category> getAll(Integer from, Integer size);

    Category findById(Long catId);

    CategoryDto create(NewCategoryDto body);

    CategoryDto update(Long categoryId, CategoryDto body);

    void deleteById(Long catId);
}
