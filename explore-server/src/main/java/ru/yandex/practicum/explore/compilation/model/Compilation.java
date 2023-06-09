package ru.yandex.practicum.explore.compilation.model;

import lombok.*;
import ru.yandex.practicum.explore.event.model.Event;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    @Column(name = "compilation_pinned")
    private Boolean pinned;

    @Column(name = "compilation_title")
    @Size(min = 3, max = 50)
    private String title;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "compilation_events",
            joinColumns = {@JoinColumn(name = "compilation_events_compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "compilation_events_event_id")})
    @ToString.Exclude
    private List<Event> events = new ArrayList<>();
}
