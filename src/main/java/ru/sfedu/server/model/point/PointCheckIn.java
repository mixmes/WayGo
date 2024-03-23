package ru.sfedu.server.model.point;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ЧекинНаТочке")
public class PointCheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "check_date")
    private Date date;

    @Column(name = "id_user", updatable = false, insertable = false)
    private long userId;

    @Column(name = "id_point", updatable = false, insertable = false)
    private long pointId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_point")
    private Point point;
}
