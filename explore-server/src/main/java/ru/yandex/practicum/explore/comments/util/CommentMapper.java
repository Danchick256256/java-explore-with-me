package ru.yandex.practicum.explore.comments.util;

import ru.yandex.practicum.explore.comments.dto.CommentDto;
import ru.yandex.practicum.explore.comments.dto.EventCommentDto;
import ru.yandex.practicum.explore.comments.model.Comment;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment dtoToComment(CommentDto body, User user, Event event) {
        return Comment.builder()
                .author(user)
                .event(event)
                .description(body.getDescription())
                .commentDate(LocalDateTime.now())
                .build();
    }

    public static EventCommentDto commentToEventDto(Comment body, User user) {
        return EventCommentDto.builder()
                .id(body.getId())
                .author(user)
                .description(body.getDescription())
                .build();
    }
}
