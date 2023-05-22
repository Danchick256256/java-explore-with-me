package ru.practicum.statistics.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.statistics.dto.HitPayload;
import ru.practicum.statistics.model.Hit;
import ru.practicum.statistics.utility.Constants;

import java.time.LocalDateTime;

@UtilityClass
public class EndPointHitMapper {

    public static Hit toHit(HitPayload hitPayload) {
        return Hit.builder()
                .id(hitPayload.getId())
                .uri(hitPayload.getUri())
                .app(hitPayload.getApp())
                .ip(hitPayload.getIp())
                .timestamp(LocalDateTime.parse(hitPayload.getTimestamp(), Constants.TIME_FORMATTER))
                .build();
    }

}
