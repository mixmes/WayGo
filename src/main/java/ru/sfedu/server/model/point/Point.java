package ru.sfedu.server.model.point;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sfedu.server.model.metainfo.ArMetaInfo;
import ru.sfedu.server.model.metainfo.AudioMetaInfo;
import ru.sfedu.server.model.metainfo.PhotoMetaInfo;
import ru.sfedu.server.model.route.Route;

import java.util.ArrayList;
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

    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    //@JoinColumn(name = "id_point")
    //private Set<PointCheckIn> checkIns;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PhotoMetaInfo> photo = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private ArMetaInfo arFileMeta;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "route_point",
            joinColumns = @JoinColumn(name = "id_point"),
            inverseJoinColumns = @JoinColumn(name = "id_route")
    )
    private List<Route> routes = new ArrayList<>();

    public void addPhotos(List<PhotoMetaInfo> metaInfos){
        this.photo.addAll(metaInfos);
    }

    public void updatePoint(Point updated){
        this.setPointName(updated.pointName);
        this.setArFileMeta(updated.arFileMeta);
        this.setAddress(updated.address);
        this.setDescription(updated.description);
        this.setLatitude(updated.latitude);
        this.setLongitude(updated.longitude);
    }


}
