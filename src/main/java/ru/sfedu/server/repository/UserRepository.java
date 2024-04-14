package ru.sfedu.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.model.route.Route;
import ru.sfedu.server.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);
    Optional<User> findByUid(String uid);

    @Query("SELECT u.favouritePoints FROM User u WHERE u.id = :userId")
    List<Point> findAllFavouritePointsById(@Param("userId") Long userId);

    @Query("SELECT u.favouriteRoutes FROM User u WHERE u.id = :userId")
    List<Route> findAllFavouriteRoutesById(@Param("userId") Long userId);
}
