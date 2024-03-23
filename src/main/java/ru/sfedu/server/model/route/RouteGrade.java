package ru.sfedu.server.model.route;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.sfedu.server.model.user.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ОценкаМаршрута")
public class RouteGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grade")
    private long id;

    @ManyToMany(mappedBy = "routeGrades")
    private Set<User> users = new HashSet<>();

    @Column(name = "id_route", insertable = false, updatable = false)
    private long routeId;

    @Column(name = "grade")
    private int grade;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_route")
    private Route route;
}
