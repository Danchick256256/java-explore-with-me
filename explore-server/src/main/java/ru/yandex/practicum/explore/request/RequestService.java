package ru.yandex.practicum.explore.request;

import ru.yandex.practicum.explore.event.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.explore.request.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.explore.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequest(Long userId, Long requestId, EventRequestStatusUpdateRequest eventDto);

    ParticipationRequestDto cancelUserRequest(Long userId, Long requestId);
}
