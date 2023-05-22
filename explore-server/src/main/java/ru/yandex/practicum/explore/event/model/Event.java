package ru.yandex.practicum.explore.event.model;

import lombok.*;
import ru.yandex.practicum.explore.category.model.Category;
import ru.yandex.practicum.explore.user.model.User;
import ru.yandex.practicum.explore.util.EventState;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "events", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_annotation", nullable = false)
    @Size(min = 20, max = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "event_category_id", nullable = false)
    private Category category;

    @Column(name = "event_confirmed_request")
    private Long confirmedRequests;

    @Column(name = "event_created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "event_description", nullable = false)
    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "event_initiator", nullable = false)
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "event_location")
    private Location location;

    @Column(name = "event_paid")
    private Boolean paid;

    @Column(name = "event_participant_limit")
    private Integer participantLimit;

    @Column(name = "event_published_on", nullable = false)
    private LocalDateTime publishedOn;

    @Column(name = "event_request_moderation")
    private Boolean requestModeration;

    @Column(name = "event_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "event_title", nullable = false)
    @Size(min = 3, max = 120)
    private String title;

    @Column(name = "event_views")
    private Long views;

    public void addView() {
        views++;
    }
}