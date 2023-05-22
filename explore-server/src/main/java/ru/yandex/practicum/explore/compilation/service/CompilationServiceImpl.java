package ru.yandex.practicum.explore.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.explore.compilation.repository.CompilationRepository;
import ru.yandex.practicum.explore.exception.NotFoundException;
import ru.yandex.practicum.explore.compilation.dto.CompilationDto;
import ru.yandex.practicum.explore.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.explore.compilation.util.CompilationDtoMapper;
import ru.yandex.practicum.explore.event.repository.EventSpecificationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.explore.compilation.util.CompilationDtoMapper.newToCompilation;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventSpecificationRepository eventRepository;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.findAllByPinned(pinned, PageRequest.of(from, size)).stream()
                .map(CompilationDtoMapper::compToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(Long compilationId) {
        return compilationRepository.findById(compilationId)
                .map(CompilationDtoMapper::compToDto)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Compilation with id=%s was not found", compilationId)));
    }

    @Override
    @Transactional
    public CompilationDto add(NewCompilationDto body) {
        return Optional.of(compilationRepository.save(
                        newToCompilation(body, eventRepository.findEventsByIdIn(body.getEvents()))))
                .map(CompilationDtoMapper::compToDto)
                .orElseThrow(() -> new NotFoundException("Unknown error"));
    }

    @Override
    @Transactional
    public void deleteById(Long compilationId) {
        compilationRepository.deleteById(compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Compilation with id=%s was not found", compilationId)))
                .getId());
    }

    @Override
    public CompilationDto updateById(long compilationId, NewCompilationDto body) {
        return compilationRepository.findById(compilationId)
                .map(compilation -> {
                    deleteById(compilationId);
                    return compilationRepository.save(newToCompilation(body, eventRepository.findEventsByIdIn(body.getEvents())));
                }).map(CompilationDtoMapper::compToDto)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Compilation with id=%s was not found", compilationId)));
    }
}
