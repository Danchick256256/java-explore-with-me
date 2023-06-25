package ru.yandex.practicum.explore.comments.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CommentDto {
    @NotBlank
    @Size(min = 3, max = 7000)
    private String description;
    private Long author;
}
