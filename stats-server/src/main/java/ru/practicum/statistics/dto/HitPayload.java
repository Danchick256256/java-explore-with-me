package ru.practicum.statistics.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HitPayload {
    private Long id;
    @NotBlank
    private String uri;
    @NotBlank
    private String app;
    @NotBlank
    private String ip;
    private String timestamp;
    private Integer hits;
}
