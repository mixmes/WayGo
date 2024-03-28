package ru.sfedu.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sfedu.server.model.point.Point;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findByCity(String city);

    @Query("SELECT p FROM Point p WHERE p.pointName LIKE %:pointName% and p.city = :city")
    List<Point> findByCityAndPointNameLike(@Param("pointName") String pointName, @Param("city") String city);
}
