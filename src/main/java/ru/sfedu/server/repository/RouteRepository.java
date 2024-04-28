package ru.sfedu.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sfedu.server.model.metainfo.AudioMetaInfo;
import ru.sfedu.server.model.route.Route;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    public List<Route> findByCity(String city);

    @Query("SELECT r FROM Route r WHERE r.routeName LIKE %:routeName% and r.city = :city")
    List<Route> findByRouteNameAndCityLike(@Param("routeName") String routeName, @Param("city") String city);

    List<Route> findByStopsOnRoute_id(Long id);

    @Query("SELECT p.audioMetaInfo FROM Route p where p.id = :id")
    Optional<AudioMetaInfo> findAudioMetaInfoByPointId(@Param("id") Long id);
}
