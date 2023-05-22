package ru.yandex.practicum.explore.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.explore.exception.BadRequestException;
import ru.yandex.practicum.explore.exception.ConditionsNotMetException;
import ru.yandex.practicum.explore.exception.NotFoundException;
import ru.yandex.practicum.explore.user.model.User;
import ru.yandex.practicum.explore.user.service.UserService;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.event.service.EventService;
import ru.yandex.practicum.explore.request.repository.RequestRepository;
import ru.yandex.practicum.explore.request.util.RequestMapper;
import ru.yandex.practicum.explore.request.dto.ParticipationRequestDto;
import ru.yandex.practicum.explore.request.model.ParticipationRequest;
import ru.yandex.practicum.explore.util.EventState;
import ru.yandex.practicum.explore.util.ParticipationStatus;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.explore.request.util.RequestMapper.getNewRequest;
import static ru.yandex.practicum.explore.request.util.RequestMapper.requestToDto;
import static ru.yandex.practicum.explore.util.ParticipationStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {

    private final EventService eventService;
    private final UserService userService;
    private final RequestRepository requestRepository;

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId) {
        userService.getUserById(userId);
        eventService.findEventById(eventId);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        userService.getUserById(userId);
        return requestRepository.findAllByRequester_Id(userId).stream()
                .map(RequestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventService.findEventById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new BadRequestException("Participation in unpublished events is denied");
        if (event.getInitiator().getId().equals(userId))
            throw new BadRequestException(
                    String.format("User id=%s is owner of event id=%s. Participation in your own events is denied",
                            userId, eventId));
        if (requestRepository.findAllByRequester_IdAndEvent_Id(userId, eventId).stream().findFirst().isPresent())
            throw new BadRequestException(
                    String.format("Request from user id=%s for event id=%s already exist.", userId, eventId));
        if (event.getParticipantLimit() != 0 && countRequests(eventId) >= event.getParticipantLimit())
            throw new ConditionsNotMetException("The limit on the number of participants has been exceeded");
        ParticipationRequest request = getNewRequest(user, event);
        if (!event.getRequestModeration()) {
            request.setStatus(CONFIRMED);
        }
        return requestToDto(requestRepository.save(request));
    }

    @Override
    public void cancelUserRequest(Long userId, Long requestId) {
        userService.getUserById(userId);
        getRequest(requestId);
        requestRepository.deleteById(requestId);
    }

    private Long countRequests(Long eventId) {

        return requestRepository.countConfirmedRequests(eventId, ParticipationStatus.CONFIRMED);
    }

    private void getRequest(Long reqId) {
        requestRepository.findById(reqId).orElseThrow(() -> new NotFoundException(
                String.format("Request with id=%s was not found", reqId)));
    }
}
