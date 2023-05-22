package ru.yandex.practicum.explore.compilation.util;

import ru.yandex.practicum.explore.compilation.dto.CompilationDto;
import ru.yandex.practicum.explore.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.explore.compilation.model.Compilation;
import ru.yandex.practicum.explore.event.util.EventDtoMapper;
import ru.yandex.practicum.explore.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationDtoMapper {

    public static CompilationDto compToDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream()
                        .map(EventDtoMapper::eventToShortDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Compilation newToCompilation(NewCompilationDto body, List<Event> events) {
        return Compilation.builder()
                .pinned(body.getPinned())
                .title(body.getTitle())
                .events(events)
                .build();
    }
}
