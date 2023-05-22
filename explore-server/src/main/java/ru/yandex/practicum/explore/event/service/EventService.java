package ru.yandex.practicum.explore.event.service;

import ru.yandex.practicum.explore.event.dto.EventFullDto;
import ru.yandex.practicum.explore.event.dto.EventShortDto;
import ru.yandex.practicum.explore.event.dto.EventSort;
import ru.yandex.practicum.explore.util.EventState;
import ru.yandex.practicum.explore.event.dto.NewEventDto;
import ru.yandex.practicum.explore.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getAllEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Boolean onlyAvailable, Integer from, Integer size,
                                     EventSort sort);

    EventFullDto getEventById(Long id);

    Event findEventById(Long eventId);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto updateUserEvent(Long eventId, Long userId, NewEventDto eventShortDto);

    EventFullDto addUserEvent(Long userId, NewEventDto eventDto);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto updateUserEventById(Long userId, Long eventId);

    List<EventFullDto> searchEvents(List<Long> users, List<EventState> states, List<Integer> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, NewEventDto body);

}
