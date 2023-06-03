package ru.yandex.practicum.explore.event.service;

import ru.yandex.practicum.explore.event.dto.EventFullDto;
import ru.yandex.practicum.explore.event.dto.EventShortDto;
import ru.yandex.practicum.explore.event.dto.UpdateEventUserRequest;
import ru.yandex.practicum.explore.util.EventSort;
import ru.yandex.practicum.explore.event.dto.NewEventDto;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.util.StateAction;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getAllEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Boolean onlyAvailable, Integer from, Integer size,
                                     EventSort sort);

    Event getEventById(Long id);

    Event findEventById(Long eventId);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    Event updateUserEvent(Long eventId, Long userId, UpdateEventUserRequest eventShortDto);

    Event addUserEvent(Long userId, NewEventDto eventDto);

    Event getUserEventById(Long userId, Long eventId);

    List<Event> searchEvents(List<Long> users, List<StateAction> states, List<Integer> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    Integer from, Integer size);

    Event updateEventByAdmin(Long eventId, UpdateEventUserRequest body);

}
