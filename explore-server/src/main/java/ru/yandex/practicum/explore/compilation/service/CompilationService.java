package ru.yandex.practicum.explore.compilation.service;

import ru.yandex.practicum.explore.compilation.dto.CompilationDto;
import ru.yandex.practicum.explore.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compilationId);

    CompilationDto add(NewCompilationDto body);

    void deleteById(Long compilationId);

    CompilationDto updateById(long compilationId, NewCompilationDto body);
}
