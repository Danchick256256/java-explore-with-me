package ru.yandex.practicum.explore.comments.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.explore.user.model.User;

@Data
@Builder
public class EventCommentDto {
    private Long id;
    private String description;
    private User author;
}
