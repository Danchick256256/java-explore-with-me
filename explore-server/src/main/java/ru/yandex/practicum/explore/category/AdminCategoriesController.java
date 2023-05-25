package ru.yandex.practicum.explore.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore.category.dto.CategoryDto;
import ru.yandex.practicum.explore.category.dto.NewCategoryDto;
import ru.yandex.practicum.explore.category.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addAdminCategory(@RequestBody @Valid NewCategoryDto body,
                                        HttpServletRequest request) {
        log.info("{className: {}, method: {POST: {}}, data: {newCategoryDto: {}}}",
                getClass().getName(), request.getRequestURI(), body);
        return categoryService.create(body);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateAdminCategory(@PathVariable Long catId,
                                           @RequestBody @Valid CategoryDto body,
                                           HttpServletRequest request) {

        log.info("{className: {}, method: {PATCH: {}}, data: {categoryDto: {}}}",
                getClass().getName(), request.getRequestURI(), body);
        return categoryService.update(catId, body);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> deleteAdminCategory(@PathVariable Long catId,
                                                      HttpServletRequest request) {
        log.info("{className: {}, method: {DELETE: {}}, data: {categoryId: {}}}",
                getClass().getName(), request.getRequestURI(), catId);
        categoryService.deleteById(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
