package ru.sfedu.server.model.route;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Table(name = "ЧекинНаМаршруте")
@Entity
public class RouteCheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_user", updatable = false, insertable = false)
    private long userId;

    @Column(name = "id_route", updatable = false, insertable = false)
    private long routeId;

    @Column(name = "check_date")
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_route")
    private Route route;
}
