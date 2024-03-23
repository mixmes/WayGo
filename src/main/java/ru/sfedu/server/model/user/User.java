package ru.sfedu.server.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.model.point.PointCheckIn;
import ru.sfedu.server.model.route.Route;
import ru.sfedu.server.model.route.RouteCheckIn;
import ru.sfedu.server.model.route.RouteGrade;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "Пользователь")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private long id;

    @Column(name = "uuid")
    private String uid;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_grade",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_grade")
    )
    private Set<RouteGrade> routeGrades = new HashSet<>();

    @JoinColumn(name = "id_user")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RouteCheckIn> routeCheckIns = new HashSet<>();

    @JoinColumn(name = "id_user")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PointCheckIn> pointCheckIns = new HashSet<>();

//    @JoinColumn(name = "id_user")
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private Set<SubscriptionTransaction> subscriptionTransactions = new HashSet<>();
//
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private Subscription subscription;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Route> favouriteRoutes = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Point> favouritePoints = new HashSet<>();

    public void addRouteGrade(RouteGrade routeGrade) {
        routeGrades.add(routeGrade);
    }

    public void addRouteCheckIn(RouteCheckIn checkIn) {
        routeCheckIns.add(checkIn);
    }

    public void addPointCheckIn(PointCheckIn checkIn) {
        pointCheckIns.add(checkIn);
    }

//    public void addSubscriptionTransaction(SubscriptionTransaction transaction) {
//        subscriptionTransactions.add(transaction);
//    }

    public void addFavouriteRoute(Route route) {
        favouriteRoutes.add(route);
    }

    public void addFavouritePoint(Point point) {
        favouritePoints.add(point);
    }

}
