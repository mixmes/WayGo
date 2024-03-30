package ru.sfedu.server.model.point;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sfedu.server.model.metainfo.PhotoMetaInfo;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_point")
    private long id;

    private String pointName;

    @Column(name = "adress")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "description")
    private String description;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "id_point")
    private Set<PointCheckIn> checkIns;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PhotoMetaInfo> photos;

}
