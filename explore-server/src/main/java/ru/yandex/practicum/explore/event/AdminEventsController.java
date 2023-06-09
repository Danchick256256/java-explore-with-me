package ru.yandex.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore.event.dto.UpdateEventUserRequest;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.event.service.EventService;
import ru.yandex.practicum.explore.util.OnCreate;
import ru.yandex.practicum.explore.util.OnUpdate;
import ru.yandex.practicum.explore.util.StateAction;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventsController {

    private final EventService eventService;

    @GetMapping
    public List<Event> getAdminEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                             @RequestParam(name = "states", required = false) List<StateAction> states,
                                             @RequestParam(name = "categories", required = false)
                                             List<Integer> categories,
                                             @RequestParam(name = "rangeStart", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") Integer size,
                                             HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {from: {}, size: {}, users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}}}",
                getClass().getName(), request.getRequestURI(), from, size, users, states, categories, rangeStart, rangeEnd);
        return eventService.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/{eventId}")
    @Validated({ OnCreate.class, OnUpdate.class })
    public Event updateAdminEvent(@PathVariable Long eventId,
                                  @RequestBody @Validated UpdateEventUserRequest body,
                                  HttpServletRequest request) {
        log.info("{className: {}, method: {PATCH: {}}, data: {eventId: {}, newEventDto: {}}}",
                getClass().getName(), request.getRequestURI(), eventId, body);
        return eventService.updateEventByAdmin(eventId, body);
    }
}
