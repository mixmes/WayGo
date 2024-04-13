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
import ru.sfedu.server.dto.converters.PointCheckInConverter;
import ru.sfedu.server.dto.converters.PointConverter;
import ru.sfedu.server.dto.metadata.ArMetadataDTO;
import ru.sfedu.server.dto.point.PointDTO;
import ru.sfedu.server.model.metainfo.MetaInfo;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.service.PointDataService;

import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    private AmazonS3 s3Client;

    @Operation(
            summary = "Получение точки",
            description = "Позволяет получить точку по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PointDTO> getPointById(@PathVariable @Parameter(description = "ID точки") Long id) throws IOException {
        Optional<Point> point = dataService.getById(id);
        if (point.isPresent()) {
            Point pointEntity = point.get();
            PointDTO dto = pointConverter.convertToDto(pointEntity);
            dto.setPhoto(convertMetaInfoToByte(pointEntity.getPhoto()));

            ArMetadataDTO ar = new ArMetadataDTO();
            ar.setArFile(convertMetaInfoToByte(pointEntity.getArFileMeta()));
            if (pointEntity.getArFileMeta() != null) {
                ar.setScale(pointEntity.getArFileMeta().getScale());
            }
            dto.setArMetadataDTO(ar);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/route")
    public List<Long> getIdsByRouteId(@RequestParam(name = "routeId") Long routeId) {
        List<Long> ids = new ArrayList<>();
        dataService.getByRouteId(routeId).forEach(s -> ids.add(s.getId()));

        return ids;
    }

    @Operation(
            summary = "Получение списка точек",
            description = "Позволяет получить список точек по названию города"
    )
    @GetMapping("/all")
    public ResponseEntity<List<PointDTO>> getAllByCity(@RequestParam @Parameter(description = "Название города") String city) {
        log.info("Get point by city={}", city);
        List<Point> points = dataService.getAllByCity(city);
        if (points.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PointDTO> pointsDtos = new ArrayList<>();
        points.forEach(s -> {
            PointDTO dto = pointConverter.convertToDto(s);
            try {
                dto.setPhoto(convertMetaInfoToByte(s.getPhoto()));

                ArMetadataDTO ar = new ArMetadataDTO();
                ar.setArFile(convertMetaInfoToByte(s.getArFileMeta()));
                if (s.getArFileMeta() != null) {
                    ar.setScale(s.getArFileMeta().getScale());
                }
                dto.setArMetadataDTO(ar);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            pointsDtos.add(dto);
        });

        return new ResponseEntity<>(pointsDtos, HttpStatus.OK);
    }

    @Operation(summary = "Получение списка точек", description = "Позволяет получить список точки по названию города и like названию точки")
    @GetMapping
    public ResponseEntity<List<PointDTO>> getByCityAndName(@RequestParam(name = "city") @Parameter(description = "Название города") String city,
                                                           @RequestParam(name = "pointName") @Parameter(description = "like название точки") String pointName) {
        log.info(city + " " + pointName);
        List<PointDTO> points = dataService.getByCityAndPointName(city, pointName).stream().map(value -> pointConverter.convertToDto(value)).toList();
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
    public ResponseEntity<?> createPoint(@RequestParam @Parameter(description = "Точка") PointDTO dto) {
        dataService.save(pointConverter.convertToEntity(dto));
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


    private byte[] convertMetaInfoToByte(MetaInfo metaInfo) throws IOException {
        if (metaInfo == null) {
            return null;
        }
        S3Object s3Object = getS3Object(metaInfo.getBucketName(), metaInfo.getKey());

        return convertS3objectToByteArray(s3Object);
    }

    private byte[] convertS3objectToByteArray(S3Object s3Object) throws IOException {
        return s3Object.getObjectContent().readAllBytes();
    }

    private S3Object getS3Object(String bucketName, String key) {
        return s3Client.getObject(bucketName, key);
    }
}
