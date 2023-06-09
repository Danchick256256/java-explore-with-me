package ru.yandex.practicum.explore.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class NewCategoryDto {
    @NotBlank
    @Size(max = 50)
    private String name;
}
