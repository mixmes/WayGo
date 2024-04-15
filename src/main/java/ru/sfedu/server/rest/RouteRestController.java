package ru.sfedu.server.rest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sfedu.server.dto.converters.RouteConverter;
import ru.sfedu.server.dto.route.RouteDTO;
import ru.sfedu.server.model.metainfo.PhotoMetaInfo;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.model.route.Route;
import ru.sfedu.server.service.PointDataService;
import ru.sfedu.server.service.RouteDataService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Tag(name = "Маршруты(экскурсии)", description = "Получение маршрутов")
@RestController
@RequestMapping("/api/route")
public class RouteRestController {
    private static final Logger log = LoggerFactory.getLogger(RouteRestController.class);
    @Autowired
    private RouteDataService routeDataService;

    @Autowired
    private PointDataService pointDataService;

    @Autowired
    private RouteConverter converter;

    @Autowired
    private AmazonS3 s3Client;

    @Operation(summary = "Получение маршрута", description = "ПОзволяет получить маршрут по id")
    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable @Parameter(description = "ID маршрута") Long id) throws IOException {
        Optional<Route> route = routeDataService.getById(id);
        if (route.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        RouteDTO dto = converter.convertToDto(route.get());
        for (int i = 0; i < route.get().getStopsOnRoute().size(); i++) {
            Point point = route.get().getStopsOnRoute().get(i);
            for (int j = 0; j < point.getPhoto().size(); j++) {
                dto.getStopsOnRoute().get(i).getPhoto().add(convertPhotoInfoToByte(point.getPhoto().get(j)));
            }
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/point")
    public ResponseEntity<List<RouteDTO>> getRoutesByPointId(@RequestParam(name = "pointId") Long pointId) {
        List<RouteDTO> routes = routeDataService.getByPointId(pointId).stream().map(s -> converter.convertToDto(s)).toList();
        if (routes.isEmpty()) {
            return (ResponseEntity<List<RouteDTO>>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok(routes);
    }

    @Operation(summary = "Получение списка маршрута", description = "Позволяет получить список маршрутов по названию города")
    @GetMapping("/all")
    public ResponseEntity<List<RouteDTO>> getAllByCity(@RequestParam @Parameter(description = "Название города") String city) throws IOException {
        List<Route> routes = routeDataService.getByCity(city);
        if (routes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<RouteDTO> routeDtos = routes.stream().map(value -> converter.convertToDto(value)).toList();

        for (int i = 0; i < routeDtos.size(); i++) {
            Route route = routes.get(i);
            RouteDTO dto = routeDtos.get(i);
            for (int j = 0; j < dto.getStopsOnRoute().size(); j++) {
                dto.getStopsOnRoute().get(j).setPhoto(route.getStopsOnRoute().get(j).getPhoto().stream().map(s -> {
                    try {
                        return convertPhotoInfoToByte(s);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList());
            }
        }

        return new ResponseEntity<>(routeDtos, HttpStatus.OK);
    }

    @Operation(summary = "Получение списка маршрутов", description = "Позволяет получить список маршуртов по названию города и like названию маршрута")
    @GetMapping("/")
    public ResponseEntity<List<RouteDTO>> getByCityAndName(@RequestParam(name = "city") @Parameter(description = "Название города") String city,
                                                           @RequestParam(name = "routeName") @Parameter(description = "like названию маршрута") String routeName) {
        List<RouteDTO> routes = routeDataService.getByCityAndRouteNameLike(routeName, city).stream().map(value -> converter.convertToDto(value)).toList();
        if (routes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    @Operation(summary = "Создание маршрута", description = "Позволяет создать маршрут")
    @PostMapping
    public ResponseEntity<?> createRoute(@RequestBody RouteDTO dto) {
        routeDataService.save(converter.convertToEntity(dto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновление маршрута")
    @PutMapping
    public ResponseEntity<?> updateRoute(@RequestBody RouteDTO dto) {
        routeDataService.save(converter.convertToEntity(dto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаление маршрута по id")
    @DeleteMapping
    public ResponseEntity<?> deleteRouteById(@RequestParam(name = "id") @Parameter(description = "ID маршрута") Long id) {
        log.info(String.valueOf(id));
        routeDataService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private byte[] convertPhotoInfoToByte(PhotoMetaInfo photo) throws IOException {
        S3Object s3Object = getS3Object(photo.getBucketName(), photo.getKey());
        byte[] photoBytes = convertS3objectToByteArray(s3Object);

        return photoBytes;
    }

    private byte[] convertS3objectToByteArray(S3Object s3Object) throws IOException {
        return s3Object.getObjectContent().readAllBytes();
    }

    private S3Object getS3Object(String bucketName, String key) {
        return s3Client.getObject(bucketName, key);
    }

}
