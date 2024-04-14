package ru.sfedu.server.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sfedu.server.dto.converters.*;
import ru.sfedu.server.dto.point.PointDTO;
import ru.sfedu.server.dto.route.RouteCheckInDTO;
import ru.sfedu.server.dto.route.RouteDTO;
import ru.sfedu.server.dto.route.RouteGradeDTO;
import ru.sfedu.server.dto.user.UserDTO;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.model.point.PointCheckIn;
import ru.sfedu.server.model.route.Route;
import ru.sfedu.server.model.route.RouteCheckIn;
import ru.sfedu.server.model.route.RouteGrade;
import ru.sfedu.server.model.user.User;
import ru.sfedu.server.service.PointDataService;
import ru.sfedu.server.service.RouteDataService;
import ru.sfedu.server.service.UserDataService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(
        name = "Пользователи",
        description = "Операции над пользователями"
)
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserRestController {
    @Autowired
    private UserDataService userDataService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PointConverter pointConverter;

    @Autowired
    private RouteConverter routeConverter;

    @Autowired
    private RouteDataService routeDataService;

    @Autowired
    private RouteGradeConverter routeGradeConverter;

    @Autowired
    private PointDataService pointDataService;

    @Autowired
    private SubscriptionTransactionConverter transactionConverter;

    @Operation(summary = "Получение пользователя", description = "Позволяет получить пользователя по id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable @Parameter(description = "ID пользователя") Long id) {
        Optional<User> user = userDataService.getById(id);

        return user.map(value -> new ResponseEntity<>(userConverter.convertToDto(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Оценка маршрута", description = "Позволяет пользователю оценить маршрут")
    @PostMapping(value = "/estimate", consumes = "application/json")
    public ResponseEntity<?> rateRoute(@RequestParam @Parameter(description = "ID пользователя") Long userId,
                                       @RequestParam @Parameter(description = "ID маршрута") Long routeId,
                                       @RequestBody @Parameter(description = "Оценка") RouteGradeDTO dto) {

        log.info("Rate route id={}", routeId);
        Optional<User> user = userDataService.getById(userId);
        if (user.isEmpty()) {
            log.error("There is no user with such id={}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Route> route = routeDataService.getById(routeId);
        if (route.isEmpty()) {
            log.error("There is no route with such id={}", routeId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        RouteGrade grade = routeGradeConverter.convertToEntity(dto);
        grade.setRoute(route.get());

        user.get().addRouteGrade(grade);
        userDataService.save(user.get());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Чекин на маршруте", description = "Позволяет пользователю сделать чекин на маршруте")
    @PostMapping(value = "/route/checkin", consumes = "application/json")
    public ResponseEntity<?> checkInOnRoute(@RequestParam @Parameter(description = "ID пользователя") Long userId,
                                            @RequestParam @Parameter(description = "ID маршрута") Long routeId,
                                            @RequestBody @Parameter(description = "Чекин") RouteCheckInDTO dto) {
        log.info("Check in on route id={}", routeId);
        Optional<User> user = userDataService.getById(userId);
        if (user.isEmpty()) {
            log.error("There is no user with such id={}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Route> route = routeDataService.getById(routeId);
        if (route.isEmpty()) {
            log.error("There is no route with such id={}", routeId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        RouteCheckIn checkIn = new RouteCheckIn();
        checkIn.setDate(dto.getDate());
        checkIn.setRoute(route.get());

        user.get().addRouteCheckIn(checkIn);
        userDataService.save(user.get());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Чекин на точке", description = "Позволяет пользователю сделать чекин на точке")
    @PostMapping(value = "/point/checkin", consumes = "application/json")
    public ResponseEntity<?> checkInOnPoint(@RequestParam @Parameter(description = "ID пользователя") Long userId,
                                            @RequestParam @Parameter(description = "ID точки") Long pointId,
                                            @RequestBody @Parameter(name = "Чекин") PointCheckIn dto) {
        log.info("Check in on point id={}", pointId);
        Optional<User> user = userDataService.getById(userId);
        if (user.isEmpty()) {
            log.error("There is no user with such id={}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Point> point = pointDataService.getById(pointId);
        if (point.isEmpty()) {
            log.error("There is no point with such id={}", pointId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PointCheckIn checkIn = new PointCheckIn();
        checkIn.setDate(dto.getDate());
        checkIn.setPoint(point.get());

        user.get().addPointCheckIn(checkIn);
        userDataService.save(user.get());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @PostMapping(value = "/transactions", consumes = "application/json")
//    public ResponseEntity<?> addTransaction(@RequestParam Long userId, @RequestBody SubscriptionTransactionDTO dto) {
//        log.info("Add new transaction. date={}", dto.getTransactionDate());
//        if (!dto.isStatus()) {
//            log.error("Bad transaction status");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        Optional<User> user = userDataService.getById(userId);
//        if (user.isEmpty()) {
//            log.error("There is no user with such id={}", userId);
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        SubscriptionTransaction transaction = transactionConverter.convertToEntity(dto);
//        user.get().addSubscriptionTransaction(transaction);
//        userDataService.save(user.get());
//
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

    @Operation(summary = "Добавление точки в избранное", description = "Позволяет пользователю добавить точку в избранное")
    @PostMapping(value = "/points/favourite")
    public ResponseEntity<?> addFavouritePoint(@RequestParam @Parameter(description = "ID пользователя") Long userId,
                                               @RequestParam @Parameter(description = "ID точки") Long pointId) {
        log.info("Add favourite point id={}", pointId);
        Optional<User> user = userDataService.getById(userId);
        if (user.isEmpty()) {
            log.error("There is no user with such id={}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Point> point = pointDataService.getById(pointId);
        if (point.isEmpty()) {
            log.error("There is no point with such id={}", pointId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        user.get().addFavouritePoint(point.get());
        userDataService.save(user.get());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Удаление точки из избранного", description = "Позволяет пользователю удалить точку из избранного")
    @DeleteMapping(value = "/points/favourite")
    public ResponseEntity<?> deleteFavouritePoint(@RequestParam @Parameter(description = "ID пользователя") Long userId,
                                                  @RequestParam @Parameter(description = "ID точки") Long pointId) {
        log.info("Delete favourite point id={}", pointId);
        Optional<User> user = userDataService.getById(userId);
        if (user.isEmpty()) {
            log.error("There is no user with such id={}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Point> point = pointDataService.getById(pointId);
        if (point.isEmpty()) {
            log.error("There is no point with such id={}", pointId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        user.get().deleteFavouritePoint(point.get());
        userDataService.save(user.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаление точки из избранного", description = "Позволяет пользователю удалить точку из избранного")
    @DeleteMapping(value = "/routes/favourite")
    public ResponseEntity<?> deleteFavouriteRoute(@RequestParam @Parameter(description = "ID пользователя") Long userId,
                                                  @RequestParam @Parameter(description = "ID маршрута") Long routeId) {
        log.info("Add favourite point id={}", routeId);
        Optional<User> user = userDataService.getById(userId);
        if (user.isEmpty()) {
            log.error("There is no user with such id={}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Route> route = routeDataService.getById(routeId);
        if (route.isEmpty()) {
            log.error("There is no point with such id={}", routeId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        user.get().deleteFavouriteRoute(route.get());
        userDataService.save(user.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Получвение точек из списка избранного")
    @GetMapping(value = "/points/favourite")
    public ResponseEntity<Set<PointDTO>> getFavouritePoints(@RequestParam @Parameter(name = "ID пользователя") Long userId) {
        log.info("Get favourite point userId={}", userId);
        Optional<User> user = userDataService.getById(userId);
        if (user.isEmpty()) {
            log.error("There is no user with such id={}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Set<PointDTO> response = (user.get().getFavouritePoints().stream().map(s -> pointConverter.convertToDto(s)).collect(Collectors.toSet()));
        return new ResponseEntity<>(response, HttpStatus.OK)
                ;
    }

    @Operation(summary = "Добавление маршрута в избранное", description = "Позволяет пользователю добавить маршрут в избранное")
    @PostMapping(value = "/routes/favourite")
    public ResponseEntity<?> addFavouriteRoute(@RequestParam @Parameter(description = "ID пользователя") Long userId,
                                               @RequestParam @Parameter(description = "ID маршрута") Long routeId) {
        log.info("Add favourite route id={}", routeId);
        Optional<User> user = userDataService.getById(userId);
        if (user.isEmpty()) {
            log.error("There is no user with such id={}", routeId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Route> route = routeDataService.getById(routeId);
        if (route.isEmpty()) {
            log.error("There is no route with such id={}", routeId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        user.get().addFavouriteRoute(route.get());
        userDataService.save(user.get());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Получвение маршрутов из списка избранного")
    @GetMapping(value = "/routes/favourite")
    public ResponseEntity<Set<RouteDTO>> getFavouriteRoutes(@RequestParam @Parameter(name = "ID пользователя") Long userId) {
        log.info("Get favourite point userId={}", userId);
        Optional<User> user = userDataService.getById(userId);
        if (user.isEmpty()) {
            log.error("There is no user with such id={}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Set<RouteDTO> response = (user.get().getFavouriteRoutes().stream().map(s -> routeConverter.convertToDto(s)).collect(Collectors.toSet()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/points/favourite/ids")
    public ResponseEntity<List<Long>> getFavouritePointsIds(@RequestParam(name = "userId") Long id) {
        List<Long> ids = userDataService.getFavouritePointsIds(id);
        if (ids.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @GetMapping(value = "/routes/favourite/ids")
    public ResponseEntity<List<Long>> getFavouriteRoutesIds(@RequestParam(name = "userId") Long id) {
        List<Long> ids = userDataService.getFavouriteRoutesIds(id);
        if (ids.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @Operation(summary = "Создание пользователя")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO dto) {
        userDataService.save(userConverter.convertToEntity(dto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаление пользователя по ID")
    @DeleteMapping
    public ResponseEntity<?> deleteUserById(@RequestParam @Parameter(description = "ID пользователя") Long id) {
        userDataService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserByUUID(@RequestParam String uuid) {
        log.info("Get user by uid");
        Optional<User> user = userDataService.getByUid(uuid);
        if (user.isEmpty()) {
            log.error("There is no user with such uuid={}", uuid);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userConverter.convertToDto(user.get()), HttpStatus.OK);
    }
}
