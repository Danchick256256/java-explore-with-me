package ru.yandex.practicum.explore.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore.category.dto.CategoryDto;
import ru.yandex.practicum.explore.category.util.CategoryDtoMapper;
import ru.yandex.practicum.explore.category.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {from: {}, size: {}}}",
                getClass().getName(), request.getRequestURI(), from, size);
        return service.getAll(from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable Long categoryId, HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {categoryId: {}}}",
                getClass().getName(), request.getRequestURI(), categoryId);
        return CategoryDtoMapper.categoryToDto(service.findById(categoryId));
    }
}
