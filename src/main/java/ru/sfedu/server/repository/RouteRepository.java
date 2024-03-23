package ru.sfedu.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sfedu.server.model.route.Route;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    public List<Route> findByCity(String city);
}
