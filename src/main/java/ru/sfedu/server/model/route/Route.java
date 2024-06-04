package ru.sfedu.server.model.route;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.sfedu.server.model.metainfo.AudioMetaInfo;
import ru.sfedu.server.model.point.Point;

import java.io.Serializable;
import java.util.*;

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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RouteGrade> routeGrades = new HashSet<>();

    @ElementCollection
    @CollectionTable(name="route_order_of_points",
            indexes = {@Index(columnList = "point_order")},
            joinColumns = @JoinColumn(name = "route_id"))
    @Column(name = "id")
    private List<Long> orderOfPoints = new LinkedList<>();

    //@JoinColumn(name = "id_route")
    //@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    //private Set<RouteCheckIn> routeCheckIns = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private AudioMetaInfo audioMetaInfo;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "route_point",
            joinColumns = @JoinColumn(name = "id_route"),
            inverseJoinColumns = @JoinColumn(name = "id_point")
    )
    private List<Point> stopsOnRoute = new ArrayList<>();

    public void updateRoute(Route updated) {
        this.setRouteName(updated.routeName);
        this.setDescription(updated.description);
        this.setLength(updated.length);
        this.setCity(updated.city);
    }

    public void addPoint(Point point) {
        if (!orderOfPoints.contains(point.getId())) {
            orderOfPoints.add(point.getId());
            stopsOnRoute.add(point);
        }
    }

    public void deletePoint(Point point) {
        if (orderOfPoints.contains(point.getId())) {
            orderOfPoints.remove(point.getId());
            stopsOnRoute.remove(point);
        }
    }

    public LinkedList<Point> getOrderedPoints(){
        if(orderOfPoints == null || orderOfPoints.isEmpty()){
            return new LinkedList<>(stopsOnRoute);
        }
        LinkedList<Point> points = new LinkedList<>();
        orderOfPoints.stream().forEach(id->{
            points.add(stopsOnRoute.stream().filter(s->s.getId() == id).findFirst().get());
        });

        return points;
    }
}
