package ru.yandex.practicum.explore.category.util;

import ru.yandex.practicum.explore.category.dto.NewCategoryDto;
import ru.yandex.practicum.explore.category.model.Category;
import ru.yandex.practicum.explore.category.dto.CategoryDto;

public class CategoryDtoMapper {

    public static CategoryDto categoryToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category newCategoryDtoToCategory(NewCategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }
}
