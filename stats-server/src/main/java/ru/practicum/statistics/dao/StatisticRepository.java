package ru.practicum.statistics.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statistics.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Hit, Long> {
    List<Hit> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<Hit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT (ip) FROM Hit WHERE uri = ?1")
    Integer getCountIpByUri(String uri);

    @Query("SELECT COUNT (DISTINCT ip) FROM Hit WHERE uri = ?1")
    Integer getCountOfUniqueIpByUri(String uri);
}
