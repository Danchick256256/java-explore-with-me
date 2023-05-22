package ru.yandex.practicum.explore.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
