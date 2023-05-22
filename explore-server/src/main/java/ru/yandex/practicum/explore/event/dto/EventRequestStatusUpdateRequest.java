package ru.yandex.practicum.explore.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private EventStatus status;
}
