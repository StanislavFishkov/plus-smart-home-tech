package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scenarios")
@Getter
@Setter
@ToString(of = {"id", "hubId", "name"})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hub_id")
    private String hubId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "scenario")
    @Builder.Default
    private List<Condition> conditions = new ArrayList<>();

    @OneToMany(mappedBy = "scenario")
    @Builder.Default
    private List<Action> actions = new ArrayList<>();
}