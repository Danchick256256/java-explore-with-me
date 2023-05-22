package ru.yandex.practicum.explore.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class NewCategoryDto {

    @NotBlank
    private String name;
}
