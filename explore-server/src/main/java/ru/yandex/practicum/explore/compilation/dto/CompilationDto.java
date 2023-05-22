package ru.yandex.practicum.explore.compilation.dto;

import lombok.*;
import ru.yandex.practicum.explore.event.dto.EventShortDto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class CompilationDto {
    @NotEmpty
    private List<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
