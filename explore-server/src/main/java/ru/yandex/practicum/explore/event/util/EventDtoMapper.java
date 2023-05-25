package ru.yandex.practicum.explore.event.util;

import ru.yandex.practicum.explore.category.model.Category;
import ru.yandex.practicum.explore.category.util.CategoryDtoMapper;
import ru.yandex.practicum.explore.event.dto.EventFullDto;
import ru.yandex.practicum.explore.event.dto.EventShortDto;
import ru.yandex.practicum.explore.event.dto.NewEventDto;
import ru.yandex.practicum.explore.event.dto.UpdateEventUserRequest;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.user.model.User;
import ru.yandex.practicum.explore.user.util.UserDtoMapper;
import ru.yandex.practicum.explore.util.EventState;
import ru.yandex.practicum.explore.util.StateAction;

import java.time.LocalDateTime;

public class EventDtoMapper {

    public static EventFullDto eventToEventFullDto(Event event) {
        return EventFullDto.builder()
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .build();
    }

    public static EventShortDto eventToShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryDtoMapper.categoryToDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserDtoMapper.userToShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event newDtoToEvent(NewEventDto newEventDto, Category category, User user) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0L)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(user)
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.getRequestModeration())
                .state(StateAction.PENDING)
                .title(newEventDto.getTitle())
                .views(0L)
                .build();
    }

    public static UpdateEventUserRequest eventToUpdateEventUserRequest(Event event) {
        return UpdateEventUserRequest.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory().getId())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .stateAction(event.getState())
                .title(event.getTitle())
                .build();
    }
}
