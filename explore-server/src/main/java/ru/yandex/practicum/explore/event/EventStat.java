package ru.yandex.practicum.explore.event;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface EventStat {
    ResponseEntity<Object> addHitToStatistic(HttpServletRequest request);
}
