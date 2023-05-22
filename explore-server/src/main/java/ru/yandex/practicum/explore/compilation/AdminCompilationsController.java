package ru.yandex.practicum.explore.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore.compilation.dto.CompilationDto;
import ru.yandex.practicum.explore.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.explore.compilation.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto body,
                                         HttpServletRequest request) {
        log.info("{className: {}, method: {POST: {}}, data: {newCompilationDto: {}}}",
                getClass().getName(), request.getRequestURI(), body);
        return compilationService.add(body);
    }

    @DeleteMapping("/{compilationId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable Long compilationId,
                                                       HttpServletRequest request) {
        log.info("{className: {}, method: {DELETE: {}}, data: {compilationId: {}}}",
                getClass().getName(), request.getRequestURI(), compilationId);
        compilationService.deleteById(compilationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilation(@PathVariable Long compilationId,
                                                      @RequestBody @Valid NewCompilationDto body,
                                                      HttpServletRequest request) {
        log.info("{className: {}, method: {PATCH: {}}, data: {compilationId: {}, newCompilationDto {}}}",
                getClass().getName(), request.getRequestURI(), compilationId, body);
        return compilationService.updateById(compilationId, body);
    }
}
