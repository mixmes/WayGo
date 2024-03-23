package ru.sfedu.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sfedu.server.model.point.Point;

import java.util.List;
import java.util.Set;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    public List<Point> findByCity(String city);
}
