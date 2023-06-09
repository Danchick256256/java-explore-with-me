package ru.yandex.practicum.explore.comments.service;

import ru.yandex.practicum.explore.comments.dto.CommentDto;
import ru.yandex.practicum.explore.comments.dto.EventCommentDto;
import ru.yandex.practicum.explore.comments.dto.NewCommentDto;
import ru.yandex.practicum.explore.comments.model.Comment;
import ru.yandex.practicum.explore.event.model.Event;

import java.util.List;

public interface CommentService {
    Comment add(long eventId, CommentDto commentDto, Event event);
    List<EventCommentDto> getCommentsByEventId(long eventId);
    Comment update(long commentId, long userId, NewCommentDto dto);
    void delete(long commentId, long userId);
}
