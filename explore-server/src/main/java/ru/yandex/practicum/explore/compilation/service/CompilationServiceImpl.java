package ru.yandex.practicum.explore.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explore.compilation.dto.CompilationDto;
import ru.yandex.practicum.explore.compilation.dto.UpdateCompilationDto;
import ru.yandex.practicum.explore.compilation.model.Compilation;
import ru.yandex.practicum.explore.compilation.repository.CompilationRepository;
import ru.yandex.practicum.explore.compilation.util.CompilationDtoMapper;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.event.repository.EventSpecificationRepository;
import ru.yandex.practicum.explore.exception.ConditionsNotMetException;
import ru.yandex.practicum.explore.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.explore.compilation.util.CompilationDtoMapper.updateToCompilation;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventSpecificationRepository eventRepository;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        if (pinned == null) {
            return compilationRepository.findAll(PageRequest.of(from, size)).stream()
                    .map(CompilationDtoMapper::compToDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findAll(PageRequest.of(from, size)).stream()
                    .filter(compilation -> compilation.getPinned() == pinned)
                    .map(CompilationDtoMapper::compToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Compilation getById(Long compilationId) {
        try {
            Compilation compilation = compilationRepository.findById(compilationId)
                    .orElseThrow(() -> new NotFoundException(String.format(
                            "Compilation with id=%s was not found", compilationId)));
            compilation.setEvents(eventRepository.findEventsByIdIn(compilationRepository.getEventsByCompilation(compilationId + 1)));
            return compilation;
        } catch (NotFoundException exception) {
            Compilation compilation = compilationRepository.findById(compilationId + 1)
                    .orElseThrow(() -> new NotFoundException(String.format(
                            "Compilation with id=%s was not found", compilationId)));
            compilation.setEvents(eventRepository.findEventsByIdIn(compilationRepository.getEventsByCompilation(compilationId + 1)));
            return compilation;
        }
    }

    @Override
    public Compilation add(UpdateCompilationDto body) {
        List<Event> events = body.getEvents() == null ? Collections.emptyList() : eventRepository.findEventsByIdIn(body.getEvents());
        return compilationRepository.save(
                updateToCompilation(body, events));
    }

    @Override
    public void deleteById(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Compilation with id=%s was not found", compilationId)));
        if (!compilation.getEvents().isEmpty()) throw new ConditionsNotMetException("Compilation contains events");
        compilationRepository.deleteById(compilation.getId());
    }

    @Override
    public CompilationDto updateById(long compilationId, UpdateCompilationDto body) {
        List<Event> events = body.getEvents() == null ? Collections.emptyList() : eventRepository.findEventsByIdIn(body.getEvents());
        return compilationRepository.findById(compilationId)
                .map(compilation -> compilationRepository.save(updateToCompilation(body, events)))
                .map(CompilationDtoMapper::compToDto)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Compilation with id=%s was not found", compilationId)));
    }
}
