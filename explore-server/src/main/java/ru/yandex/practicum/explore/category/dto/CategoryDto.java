package ru.yandex.practicum.explore.category.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class CategoryDto {
    private Long id;
    @NotNull
    @Size(max = 50)
    private String name;
}
