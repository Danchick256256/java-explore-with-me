package ru.yandex.practicum.explore.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(Long eventId);
}
