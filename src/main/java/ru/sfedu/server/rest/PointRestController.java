package ru.sfedu.server.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sfedu.server.dto.converters.PointCheckInConverter;
import ru.sfedu.server.dto.converters.PointConverter;
import ru.sfedu.server.dto.point.PointDTO;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.service.PointDataService;

import java.util.List;
import java.util.Optional;

@Tag(name = "Точки", description = "Получение точек")
@RestController()
@RequestMapping("/api/point")
public class PointRestController {
    private static final Logger log = LoggerFactory.getLogger(PointRestController.class);
    @Autowired
    private PointDataService dataService;

    @Autowired
    private PointConverter pointConverter;

    @Autowired
    private PointCheckInConverter pointCheckInConverter;

    @Operation(
            summary = "Получение точки",
            description = "Позволяет получить точку по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PointDTO> getPointById(@PathVariable @Parameter(description = "ID точки") Long id) {
        Optional<Point> point = dataService.getById(id);
        return point.map(value -> new ResponseEntity<>(pointConverter.convertToDto(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Получение списка точек",
            description = "Позволяет получить список точек по названию города"
    )
    @GetMapping("/all")
    public ResponseEntity<List<PointDTO>> getAllByCity(@RequestParam @Parameter(description = "Название города") String city) {
        List<PointDTO> points = dataService.getAllByCity(city).stream().map(value -> pointConverter.convertToDto(value)).toList();
        if (points.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(points, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<PointDTO>> getByCityAndName(@RequestParam(name = "city") String city,
                                                           @RequestParam(name = "pointName") String pointName) {
        log.info(city+" "+ pointName);
        List<PointDTO> points = dataService.getByCityAndPointName(city,pointName).stream().map(value -> pointConverter.convertToDto(value)).toList();
        if (points.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(points, HttpStatus.OK);
    }

    @Operation(
            summary = "Создание точки",
            description = "Позволяет создать точку"
    )
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createPoint(@RequestParam @Parameter(description = "Точка") MultipartFile dto) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновление точки",
            description = "Позволяет полностью обновить точку"
    )
    @PutMapping
    public ResponseEntity<?> updatePoint(@RequestBody @Parameter(description = "Точка") PointDTO dto) {
        dataService.save(pointConverter.convertToEntity(dto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление точки",
            description = "Позволяет удалить точку используя ее ID"
    )
    @DeleteMapping
    public ResponseEntity<?> deletePointByID(@RequestParam @Parameter(description = "ID точки") Long id) {
        dataService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
