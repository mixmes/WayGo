package ru.sfedu.server.model.route;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RouteCheckIn {

    private long id;


    private long userId;


    private long routeId;


    private Date date;


    private Route route;
}
