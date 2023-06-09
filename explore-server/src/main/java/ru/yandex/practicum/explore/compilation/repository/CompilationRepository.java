package ru.yandex.practicum.explore.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.explore.compilation.model.Compilation;

import java.util.Set;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "SELECT compilation_events_event_id FROM compilation_events WHERE compilation_events_compilation_id = ?1", nativeQuery = true)
    Set<Long> getEventsByCompilation(Long compId);
}
