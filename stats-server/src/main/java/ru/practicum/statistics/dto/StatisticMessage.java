package ru.practicum.statistics.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StatisticMessage {
    private String app;
    private String uri;
    private Integer hits;
}
