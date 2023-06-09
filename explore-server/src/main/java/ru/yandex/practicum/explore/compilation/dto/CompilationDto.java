package ru.yandex.practicum.explore.compilation.dto;

import lombok.*;
import ru.yandex.practicum.explore.event.dto.EventShortDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
    @Size(min = 3, max = 50)
    private String title;
}
