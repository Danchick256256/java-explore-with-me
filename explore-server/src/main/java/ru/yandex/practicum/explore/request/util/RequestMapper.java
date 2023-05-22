package ru.yandex.practicum.explore.request.util;

import ru.yandex.practicum.explore.user.model.User;
import ru.yandex.practicum.explore.util.ParticipationStatus;
import ru.yandex.practicum.explore.event.model.Event;
import ru.yandex.practicum.explore.request.dto.ParticipationRequestDto;
import ru.yandex.practicum.explore.request.model.ParticipationRequest;

import java.time.LocalDateTime;

public class RequestMapper {

    public static ParticipationRequestDto requestToDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }

    public static ParticipationRequest getNewRequest(User user, Event event) {
        return ParticipationRequest.builder()
                .requester(user)
                .event(event)
                .status(ParticipationStatus.PENDING)
                .created(LocalDateTime.now())
                .build();
    }
}
