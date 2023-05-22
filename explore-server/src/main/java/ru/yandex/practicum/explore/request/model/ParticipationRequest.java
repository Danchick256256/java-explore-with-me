package ru.yandex.practicum.explore.request.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.explore.user.model.User;
import ru.yandex.practicum.explore.util.ParticipationStatus;
import ru.yandex.practicum.explore.event.model.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "requests", schema = "public")
@Jacksonized
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requests_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requests_requester", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requests_event", nullable = false)
    private Event event;

    @Column(name = "requests_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;

    @Column(name = "requests_created", nullable = false)
    private LocalDateTime created;
}
