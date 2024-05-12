package ru.sfedu.server.rest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sfedu.server.dto.converters.*;
import ru.sfedu.server.dto.metadata.ArMetaInfoDTO;
import ru.sfedu.server.dto.metadata.AudioMetaInfoDto;
import ru.sfedu.server.dto.metadata.MetaInfoDto;
import ru.sfedu.server.dto.metadata.PhotoMetadataInfoDto;
import ru.sfedu.server.dto.point.PointDTO;
import ru.sfedu.server.model.metainfo.ArMetaInfo;
import ru.sfedu.server.model.metainfo.AudioMetaInfo;
import ru.sfedu.server.model.metainfo.MetaInfo;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.service.PointDataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    ArMetaInfoConverter arMetaInfoConverter;


    @Autowired
    MetaInfoConverter metaInfoConverter;

    @Autowired
    PhotoMetaInfoConverter photoMetaInfoConverter;

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

            dto.setPhoto(new ArrayList<>(pointEntity.getPhoto().size()));
            pointEntity.getPhoto().forEach(s -> {
                try {
                    dto.getPhoto().add(convertMetaInfoToByte(s));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Получение id точек по id маршрута")
    @GetMapping("/route")
    public List<Long> getIdsByRouteId(@RequestParam(name = "routeId") @Parameter(description = "ID маршрута") Long routeId) {
        List<Long> ids = new ArrayList<>();
        dataService.getByRouteId(routeId).forEach(s -> ids.add(s.getId()));

        return ids;
    }

    @Operation(summary = "Получение ArMetaInfo по id точки")
    @GetMapping("/ar")
    public ResponseEntity<ArMetaInfoDTO> getArMetaInfo(@RequestParam(name = "pointId") Long pointId) {
        Optional<ArMetaInfo> ar = dataService.getArMetaInfoByPointId(pointId);
        return ar.map(arMetaInfo -> new ResponseEntity<>(arMetaInfoConverter.convertToDto(arMetaInfo), HttpStatus.OK)).orElseGet(() -> (ResponseEntity<ArMetaInfoDTO>) ResponseEntity.notFound());
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
            dto.setPhoto(new ArrayList<>(s.getPhoto().size()));
            s.getPhoto().forEach(photo -> {
                try {
                    dto.getPhoto().add(convertMetaInfoToByte(photo));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            pointsDtos.add(dto);
        });

        return new ResponseEntity<>(pointsDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Создание списка точек",
            description = "Позволяет создать множество точек"
    )
    @PostMapping("/all")
    public ResponseEntity<?> saveListPoints(@RequestBody @Parameter(description = "Список точек") List<PointDTO> points) {
        for(int i = 0; i<points.size() ; i++){
            dataService.save(pointConverter.convertToEntity(points.get(i)));
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Получение списка точек", description = "Позволяет получить список точки по названию города и like названию точки")
    @GetMapping
    public ResponseEntity<List<PointDTO>> getByCityAndName(@RequestParam(name = "city") @Parameter(description = "Название города") String city,
                                                           @RequestParam(name = "pointName") @Parameter(description = "like название точки") String pointName) {
        log.info(city + " " + pointName);
        List<Point> pointsEntity = dataService.getByCityAndPointName(city, pointName);
        List<PointDTO> points = pointsEntity.stream().map(value -> pointConverter.convertToDto(value)).toList();
        for(int i=0; i<pointsEntity.size(); i++){
            points.get(i).setPhoto(pointsEntity.get(i).getPhoto().stream().map(s-> {
                try {
                    return convertMetaInfoToByte(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).toList());
        }
        if (points.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(points, HttpStatus.OK);
    }

    @Operation(summary = "Получение координат по названию города")
    @GetMapping("/coordinates")
    public ResponseEntity<List<PointDTO>> getCoordinatesByCityName(@RequestParam(name = "city") String city) {
        List<Point> points = dataService.getAllByCity(city);

        if (points.isEmpty()) {
            return (ResponseEntity<List<PointDTO>>) ResponseEntity.notFound();
        }


        List<PointDTO> coords = points.stream().map(s -> {
            PointDTO pointDTO = new PointDTO();
            pointDTO.setLongitude(s.getLongitude());
            pointDTO.setLatitude(s.getLatitude());

            return pointDTO;
        }).toList();

        return new ResponseEntity<>(coords, HttpStatus.OK);
    }

    @Operation(
            summary = "Создание точки",
            description = "Позволяет создать точку"
    )
    @PostMapping
    public ResponseEntity<?> createPoint(@RequestBody @Parameter(description = "Точка") PointDTO dto) {
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

    @Operation(summary = "Добавление ArMetaInfo")
    @PostMapping("/metainfo/ar")
    public ResponseEntity<?> addArMetaInfo(@RequestParam(name = "pointId") Long id , @RequestBody ArMetaInfoDTO dto){
        Optional<Point> point = dataService.getById(id);
        if(point.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        point.get().setArFileMeta(arMetaInfoConverter.convertToEntity(dto));
        dataService.save(point.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаление ArMetaInfo")
    @DeleteMapping("/metainfo/ar")
    public ResponseEntity<?> deleteArMetaInfo(@RequestParam(name = "pointId") Long id ){
        Optional<Point> point = dataService.getById(id);
        if(point.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        point.get().setArFileMeta(null);
        dataService.save(point.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @Operation(summary = "Добавление множества PhotoMetaInfo")
    @PostMapping("/metainfo/photos")
    public ResponseEntity<?> addPhotosMetaInfo(@RequestParam(name = "pointId") Long id , @RequestBody List<PhotoMetadataInfoDto> dtos){
        Optional<Point> point = dataService.getById(id);
        if(point.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        point.get().addPhotos(dtos.stream().map(s->photoMetaInfoConverter.convertToEntity(s)).collect(Collectors.toList()));
        dataService.save(point.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаление PhotoMetaInfo по названию моделей")
    @DeleteMapping("/metainfo/photos")
    public ResponseEntity<?> deletePhotosMetaInfo(@RequestParam(name = "pointId") Long id , @RequestBody List<String> names){
        Optional<Point> point = dataService.getById(id);
        if(point.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        point.get().getPhoto().forEach(s->{
            for(String name: names){
                if(s.getKey().equals(name)){
                    point.get().getPhoto().remove(s);
                }
            }
        });
        dataService.save(point.get());
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
