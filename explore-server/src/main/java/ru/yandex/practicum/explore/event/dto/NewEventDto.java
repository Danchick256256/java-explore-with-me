package ru.yandex.practicum.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.explore.event.model.Location;
import ru.yandex.practicum.explore.util.OnCreate;
import ru.yandex.practicum.explore.util.OnUpdate;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Jacksonized
public class NewEventDto {

    @Positive(groups = {OnUpdate.class})
    private Long eventId;

    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private Long category;

    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    @Size(min = 20, max = 7000)
    private String description;

    @Future(groups = {OnUpdate.class, OnCreate.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(groups = {OnCreate.class})
    private Location location;

    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private Boolean paid;

    @PositiveOrZero(groups = {OnUpdate.class, OnCreate.class})
    private Integer participantLimit;

    @NotNull(groups = {OnCreate.class})
    private Boolean requestModeration;

    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    @Size(min = 3, max = 120)
    private String title;
}
