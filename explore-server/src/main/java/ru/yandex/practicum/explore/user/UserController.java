package ru.yandex.practicum.explore.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore.event.dto.*;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.request.RequestService;
import ru.yandex.practicum.explore.request.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.explore.util.OnCreate;
import ru.yandex.practicum.explore.event.service.EventService;
import ru.yandex.practicum.explore.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
public class UserController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping("/events")
    public List<Event> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {userId: {}, from: {}, size: {}}}",
                getClass().getName(), request.getRequestURI(), userId, from, size);
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping("/events")
    @Validated({ OnCreate.class })
    @ResponseStatus(HttpStatus.CREATED)
    public Event addUserEvent(@PathVariable Long userId,
                              @RequestBody @Valid NewEventDto event,
                              HttpServletRequest request) {
        log.info("{className: {}, method: {POST: {}}, data: {userId: {}, newEventDto: {}}}",
                getClass().getName(), request.getRequestURI(), userId, event);
        return eventService.addUserEvent(userId, event);
    }

    @GetMapping("/events/{eventId}")
    public Event getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {userId: {}, eventId: {}}}",
                getClass().getName(), request.getRequestURI(), userId, eventId);
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    //@Validated({OnCreate.class})
    public Event updateUserEvent(@PathVariable Long eventId,
                                        @PathVariable Long userId,
                                        @RequestBody @Valid UpdateEventUserRequest event,
                                        HttpServletRequest request) {
        log.info("{className: {}, method: {PATCH: {}}, data: {userId: {},eventId: {} , updateEventUserRequest: {}}}",
                getClass().getName(), request.getRequestURI(), userId, eventId, event);
        return eventService.updateUserEvent(eventId, userId, event);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventsRequests(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {userId: {}, eventId: {}}}",
                getClass().getName(), request.getRequestURI(), userId, eventId);
        return requestService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventById(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest eventBody,
                                                              HttpServletRequest request) {
        log.info("{className: {}, method: {PATCH: {}}, data: {userId: {}, eventId: {}, eventRequestStatusUpdateRequest: {}}}",
                getClass().getName(), request.getRequestURI(), userId, eventId, eventBody);
        return requestService.updateRequest(userId, eventId, eventBody);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserEventsRequests(@PathVariable Long userId,
                                                               HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {userId: {}}}",
                getClass().getName(), request.getRequestURI(), userId);
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/requests")
    @Validated({ OnCreate.class })
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addUserEventRequest(@PathVariable Long userId,
                                            @RequestParam Long eventId,
                                            HttpServletRequest request) {
        log.info("{className: {}, method: {POST: {}}, data: {userId: {}, eventId: {}}}",
                getClass().getName(), request.getRequestURI(), userId, eventId);
        return requestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    @Validated({ OnCreate.class })
    public ParticipationRequestDto cancelUserEvent(@PathVariable Long userId,
                                                  @PathVariable Long requestId,
                                                  HttpServletRequest request) {
        log.info("{className: {}, method: {PATCH: {}}, data: {userId: {}, requestId: {}}}",
                getClass().getName(), request.getRequestURI(), userId, requestId);
        return requestService.cancelUserRequest(userId, requestId);
    }
}
