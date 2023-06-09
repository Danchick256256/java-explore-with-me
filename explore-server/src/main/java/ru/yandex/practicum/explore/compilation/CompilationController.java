package ru.yandex.practicum.explore.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore.compilation.dto.CompilationDto;
import ru.yandex.practicum.explore.compilation.model.Compilation;
import ru.yandex.practicum.explore.compilation.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {required: {}, from: {}, size: {}}}",
                getClass().getName(), request.getRequestURI(), pinned, from, size);
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public Compilation getCompilationById(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {compilationId: {}}}",
                getClass().getName(), request.getRequestURI(), compId);
        return compilationService.getById(compId);
    }
}
