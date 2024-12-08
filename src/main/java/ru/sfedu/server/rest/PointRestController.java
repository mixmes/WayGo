package ru.sfedu.server.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.sfedu.server.dto.converters.ArMetaInfoConverter;
import ru.sfedu.server.dto.converters.MetaInfoConverter;
import ru.sfedu.server.dto.converters.PhotoMetaInfoConverter;
import ru.sfedu.server.dto.converters.PointCheckInConverter;
import ru.sfedu.server.dto.converters.PointConverter;
import ru.sfedu.server.dto.metadata.ArMetaInfoDTO;
import ru.sfedu.server.dto.metadata.PhotoMetadataInfoDto;
import ru.sfedu.server.dto.point.PointDTO;
import ru.sfedu.server.model.metainfo.ArMetaInfo;
import ru.sfedu.server.model.metainfo.MetaInfo;
import ru.sfedu.server.model.metainfo.PhotoMetaInfo;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.service.PointDataService;
import ru.sfedu.server.service.UserDataService;

@Tag(name = "Точки", description = "Получение точек")
@RestController()
@RequestMapping("/api/point")
public class PointRestController {
    private static final Logger log = LoggerFactory.getLogger(PointRestController.class);
    @Autowired
    private PointDataService dataService;

    @Autowired
    private UserDataService userDataService;

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

//            var futures = getPhotosFutures(pointEntity.getPhoto());
//            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//            var photoBytes = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
//
//            dto.setPhoto(photoBytes);

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
        
        points.forEach((Point s) -> {
            PointDTO dto = pointConverter.convertToDto(s);
//            var futures = getPhotosFutures(s.getPhoto());
//            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//            var photoBytes = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
//
//            dto.setPhoto(photoBytes);
            
            pointsDtos.add(dto);
        });

        return new ResponseEntity<>(pointsDtos, HttpStatus.OK);
    }

    private int photosCount(List<Point> points){
        int count = 0;
        for(Point p : points){
            count += p.getPhoto().size();
        }

        return count;
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
//        for(int i=0; i<pointsEntity.size(); i++){
//            points.get(i).setPhoto(pointsEntity.get(i).getPhoto().stream().map(s-> {
//                try {
//                    return convertMetaInfoToByte(s);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }).toList());
//        }
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
        Optional<Point> point = dataService.getById(dto.getId());
        point.get().updatePoint(pointConverter.convertToEntity(dto));

        dataService.save(point.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление точки",
            description = "Позволяет удалить точку используя ее ID"
    )
    @DeleteMapping
    public ResponseEntity<?> deletePointByID(@RequestParam @Parameter(description = "ID точки") Long id) {
        Optional<Point> point = dataService.getById(id);
        if(point.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userDataService.getAll().forEach(s->{
            s.deleteFavouritePoint(point.get());
            userDataService.save(s);
        });
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
    public ResponseEntity<?> addPhotosMetaInfo(@RequestParam(name = "pointId") Long id ,@Parameter(name = "название моделей") @RequestBody List<PhotoMetadataInfoDto> dtos){
        Optional<Point> point = dataService.getById(id);
        if(point.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        point.get().addPhotos(dtos.stream().map(s->photoMetaInfoConverter.convertToEntity(s)).collect(Collectors.toList()));
        dataService.save(point.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаление фото по названию")
    @DeleteMapping("/metainfo/photos")
    public ResponseEntity<?> deletePhoto(@RequestParam(name ="pointId") Long pointId,
                                          @RequestParam(name = "photo")  String photo){
        Optional<Point> point = dataService.getById(pointId);
        if(point.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        point.get().getPhoto().removeIf(s-> s.getKey().equals(photo));
        dataService.save(point.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/metainfos/photos")
    public ResponseEntity<?> getPhotosByPointId(@RequestParam Long pointId){
        List<PhotoMetaInfo> metainfos = dataService.getPhotoMetaInfosById(pointId);
        var futures = getPhotosFutures(metainfos);

        var photoBytes = futures.stream().map(future -> {
            byte[] photo = null;
            try {
                photo = future.get();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return photo;
        }).collect(Collectors.toList());
        
        return new ResponseEntity<>(photoBytes, HttpStatus.OK);
    }

    private List<CompletableFuture<byte[]>> getPhotosFutures(List<PhotoMetaInfo> metaInfos){
        ExecutorService executor = Executors.newFixedThreadPool(metaInfos.size());
        List<CompletableFuture<byte[]>> futures = metaInfos.stream().map(
            info -> CompletableFuture.supplyAsync(
                () -> {
                    S3Object s3Object = getS3Object(info.getBucketName(), info.getKey());
                    byte[] bytes = null;
                    try {
                        bytes =  convertS3objectToByteArray(s3Object);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return bytes;
                },executor)
                
        ).toList();

        return futures;
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
