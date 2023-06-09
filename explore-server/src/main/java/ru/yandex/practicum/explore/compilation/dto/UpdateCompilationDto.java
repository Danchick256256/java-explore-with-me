package ru.yandex.practicum.explore.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UpdateCompilationDto {
    private Set<Long> events;
    @Builder.Default()
    private Boolean pinned = false;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 50)
    private String title;
}
