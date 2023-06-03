package ru.yandex.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.explore.category.service.CategoryService;
import ru.yandex.practicum.explore.event.dto.EventFullDto;
import ru.yandex.practicum.explore.event.dto.EventShortDto;
import ru.yandex.practicum.explore.event.dto.NewEventDto;
import ru.yandex.practicum.explore.event.dto.UpdateEventUserRequest;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.event.model.Location;
import ru.yandex.practicum.explore.event.repository.EventSpecificationRepository;
import ru.yandex.practicum.explore.event.repository.LocationRepository;
import ru.yandex.practicum.explore.event.util.EventDtoMapper;
import ru.yandex.practicum.explore.exception.BadRequestException;
import ru.yandex.practicum.explore.exception.ConflictRequestException;
import ru.yandex.practicum.explore.exception.NotAllowedException;
import ru.yandex.practicum.explore.exception.NotFoundException;
import ru.yandex.practicum.explore.request.repository.RequestRepository;
import ru.yandex.practicum.explore.user.service.UserService;
import ru.yandex.practicum.explore.util.EventSort;
import ru.yandex.practicum.explore.util.ParticipationStatus;
import ru.yandex.practicum.explore.util.StateAction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.yandex.practicum.explore.event.util.EventDtoMapper.eventToEventFullDto;
import static ru.yandex.practicum.explore.event.util.EventDtoMapper.newDtoToEvent;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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
                String.format("Event with id=%s was not found", eventId)));
    }

    @Override
    @Transactional
    public Event addUserEvent(Long userId, NewEventDto eventDto) {
        Location location = Location.builder()
                .lat(eventDto.getLocation().getLat())
                .lon(eventDto.getLocation().getLon())
                .build();
        if (eventDto.getPaid() == null) eventDto.setPaid(false);
        if (eventDto.getParticipantLimit() == null) eventDto.setParticipantLimit(0);
        if (eventDto.getRequestModeration() == null) eventDto.setRequestModeration(true);
        eventDto.setLocation(locationRepository.save(location));
        return eventRepository.save(newDtoToEvent(eventDto,
                categoryService.findById(eventDto.getCategory()),
                userService.getUserById(userId)));
    }

    @Override
    public Event getUserEventById(Long userId, Long eventId) {
        userService.getUserById(userId);
        Event event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotAllowedException(
                    String.format("User with id=%s is not owner of event with id=%s", userId, eventId));
        }
        return findEventById(eventId);
    }

    @Override
    @Transactional
    public Event updateUserEvent(Long eventId, Long userId, UpdateEventUserRequest eventDto) {
        Event event = findEventById(eventId);
        if (event.getState() != null && event.getState().equals(StateAction.PUBLISHED))
            throw new DataIntegrityViolationException("Event is already published");
        if (eventDto.getStateAction() != null && eventDto.getStateAction().equals(StateAction.PUBLISHED))
            throw new DataIntegrityViolationException("Event is already published");
        if (eventDto.getStateAction() != null && eventDto.getStateAction().equals(StateAction.CANCEL_REVIEW))
            event.setState(StateAction.CANCELED);
        userService.getUserById(userId);
        if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Event is already started");
        return updateEventData(event, eventDto);
    }

    @Override
    public List<Event> searchEvents(List<Long> users, List<StateAction> states, List<Integer> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                           Integer from, Integer size) {
        if (users != null || states != null || categories != null || rangeStart != null || rangeEnd != null) {
            return specRepository.findAll(where(usersPredicate(users))
                            .and(statesPredicate(states))
                            .and(categoryPredicate(categories))
                            .and((event, cq, cb) -> cb.greaterThan(event.get("eventDate"), rangeStart))
                            .and((event, cq, cb) -> cb.lessThan(event.get("eventDate"), rangeEnd)), PageRequest.of(from, size, Sort.unsorted()))
                    .stream()
                    .collect(Collectors.toList());
        } else {
            return eventRepository.findAll(PageRequest.of(from, size, Sort.unsorted())).stream()
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public Event updateEventByAdmin(Long eventId, UpdateEventUserRequest body) {
        Event event = findEventById(eventId);

        if (body.getEventDate() != null && body.getEventDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Event is already started");

        event.setAnnotation(Optional.ofNullable(body.getAnnotation()).orElse(event.getAnnotation()));
        event.setCategory(Optional.ofNullable(body.getCategory()).map(categoryService::findById).orElse(event.getCategory()));
        event.setDescription(Optional.ofNullable(body.getDescription()).orElse(event.getDescription()));
        event.setEventDate(Optional.ofNullable(body.getEventDate()).orElse(event.getEventDate()));
        event.setPaid(Optional.ofNullable(body.getPaid()).orElse(event.getPaid()));
        event.setParticipantLimit(Optional.ofNullable(body.getParticipantLimit()).orElse(event.getParticipantLimit()));
        event.setRequestModeration(Optional.ofNullable(body.getRequestModeration()).orElse(event.getRequestModeration()));
        event.setTitle(Optional.ofNullable(body.getTitle()).orElse(event.getTitle()));

        Optional.ofNullable(body.getLocation())
                .ifPresent(location -> {
                    Location eventLocation = event.getLocation();
                    eventLocation.setLat(location.getLat());
                    eventLocation.setLon(location.getLon());
                    event.setLocation(eventLocation);
                });

        Optional.ofNullable(body.getStateAction())
                .ifPresent(stateAction -> {
                    log.info("START: EVENT " + event.getState() + ", STATE: " + stateAction);
                    if (event.getState().equals(StateAction.PUBLISHED) && stateAction.equals(StateAction.REJECT_EVENT))
                        throw new DataIntegrityViolationException("Event is already published");
                    if (event.getState().equals(StateAction.PUBLISHED) && stateAction.equals(StateAction.PUBLISH_EVENT))
                        throw new ConflictRequestException("Event is already published");
                    if (event.getState().equals(StateAction.REJECTED) && stateAction.equals(StateAction.PUBLISH_EVENT))
                        throw new DataIntegrityViolationException("Event is already published");
                    if (event.getState().equals(StateAction.PUBLISH_EVENT) && stateAction.equals(StateAction.PUBLISHED))
                        throw new DataIntegrityViolationException("Event is already published");

                    if (stateAction.equals(StateAction.SEND_TO_REVIEW))
                        event.setState(StateAction.PENDING);

                    if (stateAction.equals(StateAction.CANCEL_REVIEW))
                        event.setState(StateAction.CANCELED);

                    if (stateAction.equals(StateAction.REJECT_EVENT))
                        event.setState(StateAction.REJECTED);

                    if (stateAction.equals(StateAction.PUBLISH_EVENT))
                        event.setState(StateAction.PUBLISHED);
                    log.info("END: EVENT " + event.getState() + ", STATE: " + stateAction);
                });
        return eventRepository.save(event);
    }


    @SneakyThrows
    @Override
    public List<EventShortDto> getAllEvents(String text, List<Integer> categories, Boolean paid,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                            Integer from, Integer size, EventSort sort) {
        if (rangeEnd != null && rangeStart != null && rangeEnd.isBefore(rangeStart)) throw new ValidationException("End is Before start");
        switch (sort) {
            case VIEWS:
                return specRepository.findAll(
                                where(textPredicate(text))
                                        .and(categoryPredicate(categories))
                                        .and(paidPredicate(paid))
                                        .and(rangeEndPredicate(rangeEnd))
                                        .and(rangeStartPredicate(rangeStart))
                                        .and(availablePredicate(onlyAvailable)),
                                PageRequest.of(from, size, Sort.unsorted())).stream()
                        .sorted(Comparator.comparing(Event::getViews))
                        .map(EventDtoMapper::eventToShortDto)
                        .collect(Collectors.toList());

            case EVENT_DATE:
                return specRepository.findAll(
                                where(textPredicate(text))
                                        .and(categoryPredicate(categories))
                                        .and(paidPredicate(paid))
                                        .and(rangeEndPredicate(rangeEnd))
                                        .and(rangeStartPredicate(rangeStart))
                                        .and(availablePredicate(onlyAvailable)),
                                PageRequest.of(from, size, Sort.unsorted())).stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .map(EventDtoMapper::eventToShortDto)
                        .collect(Collectors.toList());
            default:
                return specRepository.findAll(
                                where(textPredicate(text))
                                        .and(categoryPredicate(categories))
                                        .and(paidPredicate(paid))
                                        .and(rangeStartPredicate(rangeStart))
                                        .and(rangeEndPredicate(rangeEnd))
                                        //.and((event, cq, cb) -> cb.equal(event.get("state"), StateAction.PUBLISHED))
                                        .and(availablePredicate(onlyAvailable)),
                                PageRequest.of(from, size, Sort.unsorted())).stream()
                        .sorted(Comparator.comparing(Event::getId))
                        .map(EventDtoMapper::eventToShortDto)
                        .collect(Collectors.toList());
        }
    }

    @Override
    public Event getEventById(Long id) {
        Event event = findEventById(id);
        if (!event.getState().equals(StateAction.PUBLISHED)) {
            throw new NotFoundException(String
                    .format("Event id=%s is not published", id));
        }
        Long requestCounts = requestRepository.countConfirmedRequests(id, ParticipationStatus.CONFIRMED);
        event.setConfirmedRequests(requestCounts);
        event.addView();

        eventRepository.save(event);
        return findEventById(id);
    }

    private Specification<Event> textPredicate(String text) {
        return (event, cq, cb) -> {
            if (text == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                return cb.or(cb.like(cb.lower(event.get("annotation")), "%" + text.toLowerCase() + "%"),
                        cb.like(cb.lower(event.get("description")), "%" + text.toLowerCase() + "%"));
            }
        };
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
            if (categories == null || categories.size() == 0) {
                return cb.isTrue(cb.literal(true));
            } else {
                CriteriaBuilder.In<Long> categoriesIds = cb.in(event.get("category"));
                for (long catId : categories) {
                    categoriesIds.value(catId);
                }
                return categoriesIds;
            }
        };
    }

    private Specification<Event> rangeStartPredicate(LocalDateTime rangeStart) {
        return (event, cq, cb) -> {
            if (rangeStart == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                return cb.greaterThan(event.get("eventDate"), rangeStart);
            }
        };
    }

    private Specification<Event> paidPredicate(Boolean paid) {
        return (event, cq, cb) -> {
            if (paid == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                return cb.equal(event.get("paid"), paid);
            }
        };
    }

    private Specification<Event> rangeEndPredicate(LocalDateTime rangeEnd) {
        return (event, cq, cb) -> {
            if (rangeEnd == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                return cb.lessThan(event.get("eventDate"), rangeEnd);
            }
        };
    }

    private Specification<Event> statesPredicate(List<StateAction> states) {
        return (event, cq, cb) -> {
            if (states == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                CriteriaBuilder.In<StateAction> cbStates = cb.in(event.get("state"));
                for (StateAction state : states) {
                    cbStates.value(state);
                }
                return cbStates;
            }
        };
    }

    private Specification<Event> usersPredicate(List<Long> users) {
        return (event, cq, cb) -> {
            if (users == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                CriteriaBuilder.In<Long> usersIds = cb.in(event.get("initiator"));
                for (long userId : users) {
                    usersIds.value(userId);
                }
                return usersIds;
            }
        };
    }


    private Event updateEventData(Event event, UpdateEventUserRequest dto) {
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
                .state(dto.getStateAction() == StateAction.SEND_TO_REVIEW ? StateAction.PENDING : event.getState())
                .title(dto.getTitle() != null ? dto.getTitle() : event.getTitle())
                .views(event.getViews())
                .build();
    }
}
