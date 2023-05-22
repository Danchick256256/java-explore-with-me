package ru.yandex.practicum.explore.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class NewCompilationDto {
    private Set<Long> events;
    @Builder.Default()
    private Boolean pinned = false;
    @NotBlank
    private String title;
}
