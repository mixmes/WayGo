package ru.sfedu.server.model.route;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.sfedu.server.model.point.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Route implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_route")
    private long id;

    @Column(name = "route_length")
    private long length;

    @Column(name = "name_route")
    private String routeName;

    @Column(name = "city")
    private String city;

    @Column(name = "description")
    private String description;

    @JoinColumn(name = "id_route")
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = false, fetch = FetchType.EAGER)
    private Set<RouteGrade> routeGrades = new HashSet<>();

    @JoinColumn(name = "id_route")
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = false, fetch = FetchType.EAGER)
    private Set<RouteCheckIn> routeCheckIns = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Point> stopsOnRoute = new ArrayList<>();
}