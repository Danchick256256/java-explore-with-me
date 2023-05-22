package ru.yandex.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.explore.category.service.CategoryService;
import ru.yandex.practicum.explore.event.dto.*;
import ru.yandex.practicum.explore.event.repository.LocationRepository;
import ru.yandex.practicum.explore.exception.ConditionsNotMetException;
import ru.yandex.practicum.explore.exception.NotAllowedException;
import ru.yandex.practicum.explore.exception.NotFoundException;
import ru.yandex.practicum.explore.request.repository.RequestRepository;
import ru.yandex.practicum.explore.user.service.UserService;
import ru.yandex.practicum.explore.util.EventState;
import ru.yandex.practicum.explore.util.ParticipationStatus;
import ru.yandex.practicum.explore.event.repository.EventSpecificationRepository;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.event.model.Location;
import ru.yandex.practicum.explore.event.util.EventDtoMapper;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.yandex.practicum.explore.event.util.EventDtoMapper.eventToEventFullDto;
import static ru.yandex.practicum.explore.event.util.EventDtoMapper.newDtoToEvent;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventSpecificationRepository eventRepository;
    private final EventSpecificationRepository specRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        userService.getUserById(userId);
        return eventRepository.findEventsByInitiatorId(userId, PageRequest.of(from, size, Sort.unsorted())).stream()
                .map(EventDtoMapper::eventToShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("Event with id=%s was not found.", eventId)));
    }

    @Override
    @Transactional
    public EventFullDto addUserEvent(Long userId, NewEventDto eventDto) {
        Location location = Location.builder()
                .lat(eventDto.getLocation().getLat())
                .lon(eventDto.getLocation().getLon())
                .build();
        eventDto.setLocation(locationRepository.save(location));
        return eventToEventFullDto(eventRepository.save(newDtoToEvent(eventDto,
                categoryService.findById(eventDto.getCategory()),
                userService.getUserById(userId))));
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        userService.getUserById(userId);
        Event event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotAllowedException(
                    String.format("User with id=%s is not owner of event with id=%s", userId, eventId));
        }
        return eventToEventFullDto(findEventById(eventId));
    }

    @Override
    @Transactional
    public EventFullDto updateUserEvent(Long eventId, Long userId, NewEventDto eventDto) {
        userService.getUserById(userId);
        return eventToEventFullDto(updateEventData(findEventById(eventId), eventDto));
    }

    @Override
    @Transactional
    public EventFullDto updateUserEventById(Long userId, Long eventId) {
        userService.getUserById(userId);
        Event event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotAllowedException(
                    String.format("User with id=%s is not owner of event with id=%s", userId, eventId));
        }
        if (!event.getState().equals(EventState.PUBLISHED))
            event.setState(event.getState().equals(EventState.PENDING) ? EventState.CANCELED : EventState.PENDING);
        else
            throw new NotAllowedException("Only pending or canceled events can be changed");

        return eventToEventFullDto(event);
    }

    @Override
    public List<EventFullDto> searchEvents(List<Long> users, List<EventState> states, List<Integer> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                           Integer from, Integer size) {
        if (users != null || states != null || categories != null || rangeStart != null || rangeEnd != null) {
            return specRepository.findAll(where(usersPredicate(users))
                            .and(statesPredicate(states))
                            .and(categoryPredicate(categories))
                            .and((event, cq, cb) -> cb.greaterThan(event.get("eventDate"), rangeStart))
                            .and((event, cq, cb) -> cb.lessThan(event.get("eventDate"), rangeEnd)), PageRequest.of(from, size, Sort.unsorted()))
                    .stream()
                    .map(EventDtoMapper::eventToEventFullDto)
                    .collect(Collectors.toList());
        } else {
            return eventRepository.findAll(PageRequest.of(from, size, Sort.unsorted())).stream()
                    .map(EventDtoMapper::eventToEventFullDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, NewEventDto body) {
        Event event = findEventById(eventId);
        event.setAnnotation(
                body.getAnnotation() != null ? body.getAnnotation() : event.getAnnotation());
        event.setCategory(
                body.getCategory() != null ? categoryService.findById(body.getCategory()) : event.getCategory());
        event.setDescription(
                body.getDescription() != null ? body.getDescription() : event.getDescription());
        event.setEventDate(
                body.getEventDate() != null ? body.getEventDate() : event.getEventDate());
        event.setLocation(
                body.getLocation() != null ? body.getLocation() : event.getLocation());
        event.setPaid(
                body.getPaid() != null ? body.getPaid() : event.getPaid());
        event.setParticipantLimit(
                body.getParticipantLimit() != null ? body.getParticipantLimit() : event.getParticipantLimit());
        event.setRequestModeration(
                body.getRequestModeration() != null ? body.getRequestModeration() : event.getRequestModeration());
        event.setTitle(
                body.getTitle() != null ? body.getTitle() : event.getTitle());

        return eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getAllEvents(String text, List<Integer> categories, Boolean paid,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                            Integer from, Integer size, EventSort sort) {
        switch (sort) {
            case VIEWS:
                return specRepository.findAll(
                                where(textPredicate(text))
                                        .and(categoryPredicate(categories))
                                        .and((event, cq, cb) -> cb.equal(event.get("paid"), paid))
                                        .and((event, cq, cb) -> cb.lessThan(event.get("eventDate"), rangeEnd))
                                        .and((event, cq, cb) -> cb.greaterThan(event.get("eventDate"), rangeStart))
                                        .and(availablePredicate(onlyAvailable)),
                                PageRequest.of(from, size, Sort.unsorted())).stream()
                        .sorted(Comparator.comparing(Event::getViews))
                        .map(EventDtoMapper::eventToShortDto)
                        .collect(Collectors.toList());

            case EVENT_DATE:
                return specRepository.findAll(
                                where(textPredicate(text))
                                        .and(categoryPredicate(categories))
                                        .and((event, cq, cb) -> cb.equal(event.get("paid"), paid))
                                        .and((event, cq, cb) -> cb.lessThan(event.get("eventDate"), rangeEnd))
                                        .and((event, cq, cb) -> cb.greaterThan(event.get("eventDate"), rangeStart))
                                        .and(availablePredicate(onlyAvailable)),
                                PageRequest.of(from, size, Sort.unsorted())).stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .map(EventDtoMapper::eventToShortDto)
                        .collect(Collectors.toList());
            default:
                return specRepository.findAll(
                                where(textPredicate(text))
                                        .and(categoryPredicate(categories))
                                        .and((event, cq, cb) -> cb.equal(event.get("paid"), paid))
                                        .and((event, cq, cb) -> cb.greaterThan(event.get("eventDate"), rangeStart))
                                        .and((event, cq, cb) -> cb.lessThan(event.get("eventDate"), rangeEnd))
                                        .and((event, cq, cb) -> cb.greaterThan(event.get("eventDate"), rangeStart))
                                        .and(availablePredicate(onlyAvailable)),
                                PageRequest.of(from, size, Sort.unsorted())).stream()
                        .sorted(Comparator.comparing(Event::getId))
                        .map(EventDtoMapper::eventToShortDto)
                        .collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto getEventById(Long id) {
        Event event = findEventById(id);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConditionsNotMetException(String
                    .format("Event id=%s is not published.", id));
        }
        Long requestCounts = requestRepository.countConfirmedRequests(id, ParticipationStatus.CONFIRMED);
        event.setConfirmedRequests(requestCounts);
        event.addView();

        eventRepository.save(event);
        return eventToEventFullDto(findEventById(id));
    }

    private Specification<Event> textPredicate(String text) {
        return (event, cq, cb) -> cb.or(cb.like(cb.lower(event.get("annotation")), "%" + text.toLowerCase() + "%"),
                cb.like(cb.lower(event.get("description")), "%" + text.toLowerCase() + "%"));
    }

    private Specification<Event> availablePredicate(Boolean onlyAvailable) {
        return (event, cq, cb) -> {
            if (onlyAvailable != null && onlyAvailable) {
                return cb.or(cb.le(event.get("confirmedRequests"), event.get("participantLimit")),
                        cb.le(event.get("participantLimit"), 0));
            } else {
                return cb.isTrue(cb.literal(true));
            }
        };
    }

    private Specification<Event> categoryPredicate(List<Integer> categories) {
        return (event, cq, cb) -> {
            CriteriaBuilder.In<Long> categoriesIds = cb.in(event.get("category"));
            for (long catId : categories) categoriesIds.value(catId);
            return categoriesIds;
        };
    }

    private Specification<Event> statesPredicate(List<EventState> states) {
        return (event, cq, cb) -> {
            CriteriaBuilder.In<EventState> cbStates = cb.in(event.get("state"));
            for (EventState state : states) cbStates.value(state);
            return cbStates;
        };
    }

    private Specification<Event> usersPredicate(List<Long> users) {
        return (event, cq, cb) -> {
            CriteriaBuilder.In<Long> usersIds = cb.in(event.get("initiator"));
            for (long userId : users) usersIds.value(userId);
            return usersIds;
        };
    }


    private Event updateEventData(Event event, NewEventDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation() != null ? dto.getAnnotation() : event.getAnnotation())
                .category(dto.getCategory() != null ? categoryService.findById(dto.getCategory()) : event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(dto.getDescription() != null ? dto.getDescription() : event.getDescription())
                .eventDate(dto.getEventDate() != null ? dto.getEventDate() : event.getEventDate())
                .initiator(event.getInitiator())
                .location(dto.getLocation() != null ? dto.getLocation() : event.getLocation())
                .paid(dto.getPaid() != null ? dto.getPaid() : event.getPaid())
                .participantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() : event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() : event.getRequestModeration())
                .state(event.getState())
                .title(dto.getTitle() != null ? dto.getTitle() : event.getTitle())
                .views(event.getViews())
                .build();
    }
}
