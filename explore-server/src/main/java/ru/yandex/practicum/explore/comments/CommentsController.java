package ru.yandex.practicum.explore.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore.comments.dto.CommentDto;
import ru.yandex.practicum.explore.comments.dto.NewCommentDto;
import ru.yandex.practicum.explore.comments.model.Comment;
import ru.yandex.practicum.explore.comments.service.CommentService;
import ru.yandex.practicum.explore.event.service.EventService;
import ru.yandex.practicum.explore.util.OnCreate;
import ru.yandex.practicum.explore.util.OnUpdate;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
public class CommentsController {
    private final CommentService commentService;
    private final EventService eventService;

    @PostMapping("/{id}")
    @Validated({OnCreate.class, OnUpdate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public Comment saveComment(@PathVariable Long id, @RequestBody @Validated CommentDto commentDto, HttpServletRequest request) {
        log.info("{className: {}, method: {POST: {}}, data: {id: {}, commentDto: {}}}",
                getClass().getName(), request.getRequestURI(), id, commentDto);
        return commentService.add(id, commentDto, eventService.getEventById(id));
    }

    @PatchMapping
    @Validated({OnCreate.class, OnUpdate.class})
    public Comment updateComment(@RequestBody @Validated NewCommentDto commentDto,
                                 @RequestParam(name = "comment") long comment,
                                 @RequestParam(name = "user") long user,
                                 HttpServletRequest request) {
        log.info("{className: {}, method: {PATCH: {}}, data: {user: {}, comment: {}}}",
                getClass().getName(), request.getRequestURI(), user, comment);
        return commentService.update(comment, user, commentDto);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteComment(@RequestParam(name = "comment") long comment,
                                                @RequestParam(name = "user") long user,
                                                HttpServletRequest request) {
        log.info("{className: {}, method: {DELETE: {}}, data: {user: {}, comment: {}}}",
                getClass().getName(), request.getRequestURI(), user, comment);
        commentService.delete(comment, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
