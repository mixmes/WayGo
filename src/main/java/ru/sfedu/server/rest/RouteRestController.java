package ru.sfedu.server.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sfedu.server.dto.converters.RouteConverter;
import ru.sfedu.server.dto.route.RouteDTO;
import ru.sfedu.server.model.route.Route;
import ru.sfedu.server.service.RouteDataService;

import java.util.List;
import java.util.Optional;

@Tag(name = "Маршруты(экскурсии)", description = "Получение маршрутов")
@RestController
@RequestMapping("/api/route")
public class RouteRestController {
    @Autowired
    private RouteDataService dataService;

    @Autowired
    private RouteConverter converter;

    @Operation(summary = "Получение маршрута", description = "ПОзволяет получить маршрут по id")
    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable @Parameter(description = "ID маршрута") Long id) {
        Optional<Route> route = dataService.getById(id);
        return route.map(value -> new ResponseEntity<>(converter.convertToDto(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Получение списка маршрута", description = "Позволяет получить список маршрутов по названию города")
    @GetMapping("/all")
    public ResponseEntity<List<RouteDTO>> getAllByCity(@RequestParam @Parameter(description = "Название города") String city) {
        List<RouteDTO> routes = dataService.getByCity(city).stream().map(value -> converter.convertToDto(value)).toList();
        if (routes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<RouteDTO>> getAllByCity(@RequestParam(name = "city") @Parameter(description = "Название города") String city,
                                                       @RequestParam(name = "routeName") String routeName) {
        List<RouteDTO> routes = dataService.getByCityAndRouteNameLike(routeName,city).stream().map(value -> converter.convertToDto(value)).toList();
        if (routes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    @Operation(summary = "Создание маршрута", description = "Позволяет создать маршрут")
    @PostMapping
    public ResponseEntity<?> createRoute(@RequestBody RouteDTO dto) {
        dataService.save(converter.convertToEntity(dto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновление маршрута")
    @PutMapping
    public ResponseEntity<?> updateRoute(@RequestBody RouteDTO dto) {
        dataService.save(converter.convertToEntity(dto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаление маршрута по id")
    @DeleteMapping
    public ResponseEntity<?> deleteRouteById(@RequestParam @Parameter(description = "ID маршрута") Long id) {
        dataService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
