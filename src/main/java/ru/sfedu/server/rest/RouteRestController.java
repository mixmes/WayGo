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
import ru.sfedu.server.model.route.Route;
import ru.sfedu.server.service.PointDataService;
import ru.sfedu.server.service.RouteDataService;

import java.io.IOException;
import java.util.ArrayList;
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
        for (int i = 0; i < dto.getStopsOnRoute().size(); i++) {
            dto.getStopsOnRoute().get(i).setPhotos(convertPhotosInfoToByteArrays(route.get().getStopsOnRoute().get(i).getPhotos()));
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Получение списка маршрута", description = "Позволяет получить список маршрутов по названию города")
    @GetMapping("/all")
    public ResponseEntity<List<RouteDTO>> getAllByCity(@RequestParam @Parameter(description = "Название города") String city) throws IOException {
        List<Route> routes = routeDataService.getByCity(city);
        if (routes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<RouteDTO> routeDtos = routes.stream().map(value -> converter.convertToDto(value)).toList();
        for(int i = 0; i<routeDtos.size(); i++){
            Route route = routes.get(i);
            RouteDTO dto = routeDtos.get(i);
            for (int j = 0; i < dto.getStopsOnRoute().size(); i++) {
                dto.getStopsOnRoute().get(i).setPhotos(convertPhotosInfoToByteArrays(route.getStopsOnRoute().get(i).getPhotos()));
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

    private List<byte[]> convertPhotosInfoToByteArrays(List<PhotoMetaInfo> photos) throws IOException {
        List<S3Object> objects = new ArrayList<>();
        photos.forEach(s -> objects.add(getS3Object(s.getBucketName(), s.getKey())));

        List<byte[]> photoBytes = new ArrayList<>();
        objects.forEach(s -> {
            try {
                photoBytes.add(convertS3objectToByteArray(s));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return photoBytes;
    }

    private byte[] convertS3objectToByteArray(S3Object s3Object) throws IOException {
        return s3Object.getObjectContent().readAllBytes();
    }

    private S3Object getS3Object(String bucketName, String key) {
        return s3Client.getObject(bucketName, key);
    }

}
