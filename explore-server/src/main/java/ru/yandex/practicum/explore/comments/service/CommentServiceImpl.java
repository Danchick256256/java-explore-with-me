package ru.yandex.practicum.explore.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explore.comments.dto.CommentDto;
import ru.yandex.practicum.explore.comments.dto.EventCommentDto;
import ru.yandex.practicum.explore.comments.dto.NewCommentDto;
import ru.yandex.practicum.explore.comments.model.Comment;
import ru.yandex.practicum.explore.comments.repository.CommentRepository;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.exception.BadRequestException;
import ru.yandex.practicum.explore.exception.NotFoundException;
import ru.yandex.practicum.explore.user.model.User;
import ru.yandex.practicum.explore.user.service.UserService;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static ru.yandex.practicum.explore.comments.util.CommentMapper.commentToEventDto;
import static ru.yandex.practicum.explore.comments.util.CommentMapper.dtoToComment;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Override
    public Comment add(long eventId, CommentDto commentDto, Event event) {
        return commentRepository.save(dtoToComment(commentDto,
                userService.getUserById(commentDto.getAuthor()),
                event));
    }

    @Override
    public List<EventCommentDto> getCommentsByEventId(long eventId) {
        return commentRepository.findAllByEventId(eventId).stream()
                .map(comment -> commentToEventDto(comment, comment.getAuthor()))
                .collect(toList());
    }

    @Override
    public Comment update(long commentId, long userId, NewCommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("Comment with id=%s was not found", commentId)));
        User user = userService.getUserById(userId);
        if (!Objects.equals(comment.getAuthor().getId(), user.getId()))
            throw new BadRequestException("You are not comment author");
        comment.setDescription(dto.getDescription());
        return commentRepository.save(comment);
    }

    @Override
    public void delete(long commentId, long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("Comment with id=%s was not found", commentId)));
        User user = userService.getUserById(userId);
        if (!Objects.equals(comment.getAuthor().getId(), user.getId()))
            throw new BadRequestException("You are not comment author");
        commentRepository.delete(comment);
    }
}
