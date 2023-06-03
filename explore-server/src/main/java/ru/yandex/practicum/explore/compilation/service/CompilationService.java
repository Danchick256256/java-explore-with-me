package ru.yandex.practicum.explore.compilation.service;

import ru.yandex.practicum.explore.compilation.dto.CompilationDto;
import ru.yandex.practicum.explore.compilation.dto.UpdateCompilationDto;
import ru.yandex.practicum.explore.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    Compilation getById(Long compilationId);

    Compilation add(UpdateCompilationDto body);

    void deleteById(Long compilationId);

    CompilationDto updateById(long compilationId, UpdateCompilationDto body);
}
