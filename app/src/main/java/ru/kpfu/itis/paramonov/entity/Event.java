package ru.kpfu.itis.paramonov.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private Place place;

    @Column(nullable = false)
    private Timestamp date;
}
