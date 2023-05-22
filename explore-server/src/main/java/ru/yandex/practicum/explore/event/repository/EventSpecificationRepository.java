package ru.yandex.practicum.explore.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.yandex.practicum.explore.event.model.Event;

import java.util.List;
import java.util.Set;

public interface EventSpecificationRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findEventsByCategory_Id(Long catId);

    List<Event> findEventsByInitiatorId(Long userId, Pageable pageable);

    List<Event> findEventsByIdIn(Set<Long> events);
}
