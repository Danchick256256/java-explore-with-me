package ru.yandex.practicum.explore.event.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@Builder
@Entity
@Table(name = "locations", schema = "public")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "latitude", nullable = false)
    private Double lat;

    @Column(name = "longitude", nullable = false)
    private Double lon;
}
