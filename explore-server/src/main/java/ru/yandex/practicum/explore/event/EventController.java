package ru.yandex.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.event.service.EventService;
import ru.yandex.practicum.explore.util.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final EventStat statClient;

    @GetMapping
    public List<Event> getAllEvents(@RequestParam(defaultValue = "") String text,
                                            @RequestParam(required = false) List<Integer> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {from: {}, size: {}, eventSort: {}, onlyAvailable: {}, categories: {}, rangeStart: {}, rangeEnd: {}, text: {}, paid: {}}}",
                getClass().getName(), request.getRequestURI(), from, size, sort, onlyAvailable, categories, rangeStart, rangeEnd, text, paid);

        statClient.addHitToStatistic(request);

        return eventService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size, sort);
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {id: {}}}",
                getClass().getName(), request.getRequestURI(), id);

        statClient.addHitToStatistic(request);

        return eventService.getEventById(id);
    }
}
