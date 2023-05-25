package ru.yandex.practicum.explore.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.explore.exception.NotFoundException;
import ru.yandex.practicum.explore.category.repository.CategoryRepository;
import ru.yandex.practicum.explore.category.dto.CategoryDto;
import ru.yandex.practicum.explore.category.util.CategoryDtoMapper;
import ru.yandex.practicum.explore.category.dto.NewCategoryDto;
import ru.yandex.practicum.explore.category.model.Category;
import ru.yandex.practicum.explore.event.repository.EventSpecificationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.explore.category.util.CategoryDtoMapper.categoryToDto;
import static ru.yandex.practicum.explore.category.util.CategoryDtoMapper.newCategoryDtoToCategory;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventSpecificationRepository eventRepository;

    @Override
    public Category findById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Category with id=%s was not found", catId)));
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from, size, Sort.unsorted())).stream()
                .map(CategoryDtoMapper::categoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto body) {
        return categoryToDto(categoryRepository.save(newCategoryDtoToCategory(body)));
    }

    @Override
    @Transactional
    public CategoryDto update(Long categoryId, CategoryDto body) {
        Category category = findById(categoryId);
        category.setName(body.getName());
        return categoryToDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteById(Long catId) {
        findById(catId);
        if (eventRepository.findEventsByCategory_Id(catId).stream().findAny().isEmpty()) {
            categoryRepository.deleteById(catId);
        } else
            throw new DataIntegrityViolationException("For the requested operation the conditions are not met.");
    }
}
